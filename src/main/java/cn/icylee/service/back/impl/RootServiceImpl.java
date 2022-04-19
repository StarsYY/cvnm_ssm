package cn.icylee.service.back.impl;

import cn.icylee.bean.CategoryExample;
import cn.icylee.bean.Root;
import cn.icylee.bean.RootExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.CategoryMapper;
import cn.icylee.dao.RootMapper;
import cn.icylee.service.back.RootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RootServiceImpl implements RootService {

    @Autowired
    RootMapper rootMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public int getRootTotal(TableParameter tableParameter) {
        RootExample rootExample = new RootExample();
        RootExample.Criteria criteria = rootExample.createCriteria();
        if (tableParameter.getRoot() != null) {
            criteria.andRootLike("%" + tableParameter.getRoot() + "%");
        }
        return rootMapper.countByExample(rootExample);
    }

    @Override
    public List<Root> getPageRoot(TableParameter tableParameter) {
        return rootMapper.getRootList(tableParameter);
    }

    @Override
    public Root saveRoot(Root root) {
        RootExample rootExample = new RootExample();
        RootExample.Criteria criteria = rootExample.createCriteria();
        criteria.andRootEqualTo(root.getRoot());
        if (rootMapper.selectByExample(rootExample).size() > 0) {
            return null;
        }

        root.setCreatetime(new Date());
        root.setUpdatetime(new Date());

        rootMapper.insert(root);

        return root;
    }

    @Override
    public Root updateRoot(Root root) {
        RootExample rootExample = new RootExample();
        RootExample.Criteria criteria = rootExample.createCriteria();
        criteria.andRootNotEqualTo(rootMapper.selectByPrimaryKey(root.getId()).getRoot()).andRootEqualTo(root.getRoot());
        if (rootMapper.selectByExample(rootExample).size() > 0) {
            return null;
        }

        root.setUpdatetime(new Date());

        int num = rootMapper.updateByPrimaryKeySelective(root);

        return num > 0 ? root : null;
    }

    @Override
    public int deleteRoot(int id) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andRootidEqualTo(id);
        return categoryMapper.countByExample(categoryExample) == 0 ? rootMapper.deleteByPrimaryKey(id) : -1;
    }

}
