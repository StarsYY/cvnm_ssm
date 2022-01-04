package cn.icylee.service.back.impl;

import cn.icylee.bean.Category;
import cn.icylee.bean.CategoryExample;
import cn.icylee.bean.Root;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.CategoryMapper;
import cn.icylee.dao.RootMapper;
import cn.icylee.service.back.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    RootMapper rootMapper;

    @Override
    public Map<String, Object> getAllRoot() {
        Map<String, Object> allRoot = new HashMap<>();
        List<Root> rootList = rootMapper.selectByExample(null);
        for (Root root : rootList) {
            allRoot.put(String.valueOf(root.getId()), root.getRoot());
        }
        return allRoot;
    }

    @Override
    public int getCategoryTotal(TableParameter tableParameter) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (tableParameter.getCategory() != null) {
            criteria.andCategoryLike("%" + tableParameter.getCategory() + "%");
        }
        if (tableParameter.getRootid() != null && !tableParameter.getRootid().equals("")) {
            criteria.andRootidEqualTo(Integer.parseInt(tableParameter.getRootid()));
        }
        return categoryMapper.countByExample(categoryExample);
    }

    @Override
    public List<Category> getPageCategory(TableParameter tableParameter) {
        List<Category> categoryList = categoryMapper.getCategoryList(tableParameter);
        for (Category category : categoryList) {
            category.setSuperior(rootMapper.selectByPrimaryKey(category.getRootid()).getRoot());
        }
        return categoryList;
    }

    @Override
    public int saveCategory(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andCategoryEqualTo(category.getCategory());
        if (categoryMapper.selectByExample(categoryExample).size() > 0) {
            return 0;
        }
        category.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        category.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return categoryMapper.insert(category);
    }

    @Override
    public int updateCategory(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andCategoryNotEqualTo(categoryMapper.selectByPrimaryKey(category.getId()).getCategory()).andCategoryEqualTo(category.getCategory());
        if (categoryMapper.selectByExample(categoryExample).size() > 0) {
            return 0;
        }
        category.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public int deleteCategory(int id) {
        return categoryMapper.selectByPrimaryKey(id) != null ? categoryMapper.deleteByPrimaryKey(id) : 0;
    }

}
