package cn.icylee.service.back.impl;

import cn.icylee.bean.Rotations;
import cn.icylee.bean.RotationsExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.RotationsMapper;
import cn.icylee.service.back.RotationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RotationsServiceImpl implements RotationsService {

    @Autowired
    RotationsMapper rotationsMapper;

    @Override
    public int getRotationsTotal(TableParameter tableParameter) {
        RotationsExample rotationsExample = new RotationsExample();
        RotationsExample.Criteria criteria = rotationsExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        return rotationsMapper.countByExample(rotationsExample);
    }

    @Override
    public List<Rotations> getPageRotations(TableParameter tableParameter) {
        RotationsExample rotationsExample = new RotationsExample();
        RotationsExample.Criteria criteria = rotationsExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        rotationsExample.setOrderByClause(" id " + tableParameter.getSort() + " limit " + tableParameter.getPage() + ", " + tableParameter.getLimit());
        return rotationsMapper.selectByExample(rotationsExample);
    }

    @Override
    public Rotations saveRotations(Rotations rotations) {
        RotationsExample rotationsExample = new RotationsExample();
        rotationsExample.createCriteria().andNameEqualTo(rotations.getName());
        if (rotationsMapper.selectByExample(rotationsExample).size() > 0) {
            return null;
        }

        rotations.setCreatetime(new Date());
        rotations.setUpdatetime(new Date());

        rotationsMapper.insert(rotations);
        return rotations;
    }

    @Override
    public int updateRotations(Rotations rotations) {
        RotationsExample rotationsExample = new RotationsExample();
        RotationsExample.Criteria criteria = rotationsExample.createCriteria();
        criteria.andNameNotEqualTo(rotationsMapper.selectByPrimaryKey(rotations.getId()).getName()).andNameEqualTo(rotations.getName());
        if (rotationsMapper.selectByExample(rotationsExample).size() > 0) {
            return 0;
        }
        rotations.setUpdatetime(new Date());

        return rotationsMapper.updateByPrimaryKeySelective(rotations);
    }

    @Override
    public int deleteRotations(int id) {
        return rotationsMapper.deleteByPrimaryKey(id);
    }

}
