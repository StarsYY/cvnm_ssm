package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.ModularMapper;
import cn.icylee.service.back.ModularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ModularServiceImpl implements ModularService {

    @Autowired
    ModularMapper modularMapper;

    @Override
    public int getModularTotal(TableParameter tableParameter) {
        ModularExample modularExample = new ModularExample();
        ModularExample.Criteria criteria = modularExample.createCriteria();
        if (tableParameter.getModular() != null) {
            criteria.andModularLike("%" + tableParameter.getModular() + "%");
        }
        return modularMapper.countByExample(modularExample);
    }

    @Override
    public List<Modular> getPageModular(TableParameter tableParameter) {
        List<Modular> modularList = modularMapper.getModularList(tableParameter);
        for (Modular modular : modularList) {
            modular.setSuperior(modular.getAncestor() > 0 ? modularMapper.selectByPrimaryKey(modular.getAncestor()).getModular() : "");
        }
        return modularMapper.getModularList(tableParameter);
    }

    @Override
    public List<LabelTree> getOptionModular(int children) {
        ModularExample modularExample = new ModularExample();
        ModularExample.Criteria criteria = modularExample.createCriteria();
        criteria.andAncestorEqualTo(children);
        
        List<Modular> modularList = modularMapper.selectByExample(modularExample);

        List<LabelTree> optionModular = new ArrayList<>();

        for (Modular modular : modularList) {
            LabelTree p = new LabelTree();
            p.setValue(modular.getId());
            p.setLabel(modular.getModular());

            if (getOptionModular(modular.getId()).size() != 0) {
                p.setChildren(getOptionModular(modular.getId()));
            } else {
                p.setChildren(null);
            }
            optionModular.add(p);
        }
        return optionModular;
    }

    @Override
    public Modular saveModular(Modular modular) {
        ModularExample modularExample = new ModularExample();
        ModularExample.Criteria criteria = modularExample.createCriteria();
        criteria.andModularEqualTo(modular.getModular());
        if (modularMapper.selectByExample(modularExample).size() > 0) {
            return null;
        }

        modular.setCreatetime(new Date());
        modular.setUpdatetime(new Date());
        modular.setSuperior(modular.getAncestor() > 0 ? modularMapper.selectByPrimaryKey(modular.getAncestor()).getModular() : "");

        return modularMapper.insert(modular) > 0 ? modular : null;
    }

    @Override
    public Modular updateModular(Modular modular) {
        ModularExample modularExample = new ModularExample();
        ModularExample.Criteria criteria = modularExample.createCriteria();
        criteria.andModularNotEqualTo(modularMapper.selectByPrimaryKey(modular.getId()).getModular()).andModularEqualTo(modular.getModular());
        if (modularMapper.selectByExample(modularExample).size() > 0) {
            return null;
        }
        modular.setUpdatetime(new Date());
        modular.setSuperior(modular.getAncestor() > 0 ? modularMapper.selectByPrimaryKey(modular.getAncestor()).getModular() : "");

        return modularMapper.updateByPrimaryKeySelective(modular) > 0 ? modular : null;
    }

    @Override
    public int deleteModular(int id) {
        /*
        ??????????????????????????????????????????
         */
        ModularExample modularExample = new ModularExample();
        modularExample.createCriteria().andAncestorEqualTo(id);

        if (modularMapper.countByExample(modularExample) == 0) {
            return modularMapper.deleteByPrimaryKey(id);
        } else {
            return -1;
        }
    }

}
