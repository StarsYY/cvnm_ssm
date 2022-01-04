package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.bean.PlateExample;
import cn.icylee.dao.PlateMapper;
import cn.icylee.service.back.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlateServiceImpl implements PlateService {

    @Autowired
    PlateMapper plateMapper;

    @Override
    public int getPlateTotal(TableParameter tableParameter) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        if (tableParameter.getPlate() != null) {
            criteria.andPlateLike("%" + tableParameter.getPlate() + "%");
        }
        return plateMapper.countByExample(plateExample);
    }

    @Override
    public List<Plate> getPagePlate(TableParameter tableParameter) {
        List<Plate> plateList = plateMapper.getPlateList(tableParameter);
        for (Plate plate : plateList) {
            if (plate.getAncestor() == 0) {
                plate.setSuperior("");
            } else {
                plate.setSuperior(plateMapper.selectByPrimaryKey(plate.getAncestor()).getPlate());
            }
        }
        return plateList;
    }

    @Override
    public List<LabelTree> getOptionPlate(int children) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andAncestorEqualTo(children);
        List<Plate> plateList = plateMapper.selectByExample(plateExample);
        List<LabelTree> optionPlate = new ArrayList<>();
        for (Plate plate : plateList) {
            LabelTree p = new LabelTree();
            p.setValue(plate.getId());
            p.setLabel(plate.getPlate());
            if (getOptionPlate(plate.getId()).size() != 0) {
                p.setChildren(getOptionPlate(plate.getId()));
            } else {
                p.setChildren(null);
            }
            optionPlate.add(p);
        }
        return optionPlate;
    }

    @Override
    public int savePlate(Plate plate) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andPlateEqualTo(plate.getPlate());
        if (plateMapper.selectByExample(plateExample).size() > 0) {
            return 0;
        }
        plate.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        plate.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return plateMapper.insert(plate);
    }

    @Override
    public int updatePlate(Plate plate) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andPlateNotEqualTo(plateMapper.selectByPrimaryKey(plate.getId()).getPlate()).andPlateEqualTo(plate.getPlate());
        if (plateMapper.selectByExample(plateExample).size() > 0) {
            return 0;
        }
        plate.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return plateMapper.updateByPrimaryKeySelective(plate);
    }

    @Override
    public int deletePlate(int id) {
        return plateMapper.selectByPrimaryKey(id) != null ? plateMapper.deleteByPrimaryKey(id) : 0;
    }
}
