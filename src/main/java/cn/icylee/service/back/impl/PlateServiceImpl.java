package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.bean.PlateExample;
import cn.icylee.dao.ArticleMapper;
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

    @Autowired
    ArticleMapper articleMapper;

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

            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andPlateidEqualTo(plate.getId());
            plate.setArticleCount(articleMapper.countByExample(articleExample));
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
    public Plate savePlate(Plate plate) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andPlateEqualTo(plate.getPlate());
        if (plateMapper.selectByExample(plateExample).size() > 0) {
            return null;
        }

        plate.setCreatetime(new Date());
        plate.setUpdatetime(new Date());

        plateMapper.insert(plate);

        if (plate.getAncestor() == 0) {
            plate.setSuperior("");
        } else {
            plate.setSuperior(plateMapper.selectByPrimaryKey(plate.getAncestor()).getPlate());
        }

        plate.setArticleCount(0);

        return plate;
    }

    @Override
    public int updatePlate(Plate plate) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andPlateNotEqualTo(plateMapper.selectByPrimaryKey(plate.getId()).getPlate()).andPlateEqualTo(plate.getPlate());
        if (plateMapper.selectByExample(plateExample).size() > 0) {
            return 0;
        }
        plate.setUpdatetime(new Date());

        return plateMapper.updateByPrimaryKeySelective(plate);
    }

    @Override
    public int deletePlate(int id) {
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andPlateidEqualTo(id);

        PlateExample plateExample = new PlateExample();
        plateExample.createCriteria().andAncestorEqualTo(id);

        if (articleMapper.countByExample(articleExample) == 0 && plateMapper.countByExample(plateExample) == 0) {
            return plateMapper.deleteByPrimaryKey(id);
        } else if (articleMapper.countByExample(articleExample) != 0) {
            return -1;
        } else {
            return -2;
        }
    }
}
