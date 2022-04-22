package cn.icylee.service.back.impl;

import cn.icylee.bean.Navigation;
import cn.icylee.bean.NavigationExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.NavigationMapper;
import cn.icylee.service.back.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NavigationServiceImpl implements NavigationService {

    @Autowired
    NavigationMapper navigationMapper;

    @Override
    public int getNavigationTotal(TableParameter tableParameter) {
        NavigationExample navigationExample = new NavigationExample();
        NavigationExample.Criteria criteria = navigationExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        return navigationMapper.countByExample(navigationExample);
    }

    @Override
    public List<Navigation> getPageNavigation(TableParameter tableParameter) {
        NavigationExample navigationExample = new NavigationExample();
        NavigationExample.Criteria criteria = navigationExample.createCriteria();
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            criteria.andNameLike("%" + tableParameter.getName() + "%");
        }
        navigationExample.setOrderByClause(" id " + tableParameter.getSort() + " limit " + tableParameter.getPage() + ", " + tableParameter.getLimit());
        return navigationMapper.selectByExample(navigationExample);
    }

    @Override
    public Navigation saveNavigation(Navigation navigation) {
        NavigationExample navigationExample = new NavigationExample();
        navigationExample.createCriteria().andNameEqualTo(navigation.getName());
        if (navigationMapper.selectByExample(navigationExample).size() > 0) {
            return null;
        }

        navigation.setCreatetime(new Date());
        navigation.setUpdatetime(new Date());

        navigationMapper.insert(navigation);
        return navigation;
    }

    @Override
    public int updateNavigation(Navigation navigation) {
        NavigationExample navigationExample = new NavigationExample();
        NavigationExample.Criteria criteria = navigationExample.createCriteria();
        criteria.andNameNotEqualTo(navigationMapper.selectByPrimaryKey(navigation.getId()).getName()).andNameEqualTo(navigation.getName());
        if (navigationMapper.selectByExample(navigationExample).size() > 0) {
            return 0;
        }
        navigation.setUpdatetime(new Date());

        return navigationMapper.updateByPrimaryKeySelective(navigation);
    }

    @Override
    public int deleteNavigation(int id) {
        return navigationMapper.deleteByPrimaryKey(id);
    }
    
}
