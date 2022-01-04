package cn.icylee.service.back.impl;

import cn.icylee.bean.Root;
import cn.icylee.bean.RootExample;
import cn.icylee.bean.TableParameter;
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
    public int saveRoot(Root root) {
        RootExample rootExample = new RootExample();
        RootExample.Criteria criteria = rootExample.createCriteria();
        criteria.andRootEqualTo(root.getRoot());
        if (rootMapper.selectByExample(rootExample).size() > 0) {
            return 0;
        }
        root.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        root.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return rootMapper.insert(root);
    }

    @Override
    public int updateRoot(Root root) {
        RootExample rootExample = new RootExample();
        RootExample.Criteria criteria = rootExample.createCriteria();
        criteria.andRootNotEqualTo(rootMapper.selectByPrimaryKey(root.getId()).getRoot()).andRootEqualTo(root.getRoot());
        if (rootMapper.selectByExample(rootExample).size() > 0) {
            return 0;
        }
        root.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return rootMapper.updateByPrimaryKeySelective(root);
    }

    @Override
    public int deleteRoot(int id) {
        return rootMapper.selectByPrimaryKey(id) != null ? rootMapper.deleteByPrimaryKey(id) : 0;
    }

}
