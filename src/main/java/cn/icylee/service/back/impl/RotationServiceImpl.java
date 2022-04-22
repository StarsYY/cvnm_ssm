package cn.icylee.service.back.impl;

import cn.icylee.bean.Rotation;
import cn.icylee.bean.RotationExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.RotationMapper;
import cn.icylee.service.back.RotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RotationServiceImpl implements RotationService {

    @Autowired
    RotationMapper rotationMapper;

    @Override
    public int getRotationTotal(TableParameter tableParameter) {
        RotationExample rotationExample = new RotationExample();
        RotationExample.Criteria criteria = rotationExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        return rotationMapper.countByExample(rotationExample);
    }

    @Override
    public List<Rotation> getPageRotation(TableParameter tableParameter) {
        RotationExample rotationExample = new RotationExample();
        RotationExample.Criteria criteria = rotationExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        rotationExample.setOrderByClause(" id " + tableParameter.getSort() + " limit " + tableParameter.getPage() + ", " + tableParameter.getLimit());
        return rotationMapper.selectByExample(rotationExample);
    }

    @Override
    public Rotation saveRotation(Rotation rotation) {
        RotationExample rotationExample = new RotationExample();
        rotationExample.createCriteria().andNameEqualTo(rotation.getName());
        if (rotationMapper.selectByExample(rotationExample).size() > 0) {
            return null;
        }

        rotation.setCreatetime(new Date());
        rotation.setUpdatetime(new Date());

        rotationMapper.insert(rotation);
        return rotation;
    }

    @Override
    public int updateRotation(Rotation rotation) {
        RotationExample rotationExample = new RotationExample();
        RotationExample.Criteria criteria = rotationExample.createCriteria();
        criteria.andNameNotEqualTo(rotationMapper.selectByPrimaryKey(rotation.getId()).getName()).andNameEqualTo(rotation.getName());
        if (rotationMapper.selectByExample(rotationExample).size() > 0) {
            return 0;
        }
        rotation.setUpdatetime(new Date());

        return rotationMapper.updateByPrimaryKeySelective(rotation);
    }

    @Override
    public int deleteRotation(int id) {
        return rotationMapper.deleteByPrimaryKey(id);
    }
    
}
