package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CategoryMapper;
import cn.icylee.dao.LabelMapper;
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

    @Autowired
    LabelMapper labelMapper;

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
    public Category saveCategory(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andCategoryEqualTo(category.getCategory());
        if (categoryMapper.selectByExample(categoryExample).size() > 0) {
            return null;
        }
        category.setCreatetime(new Date());
        category.setUpdatetime(new Date());

        categoryMapper.insert(category);

        category.setSuperior(rootMapper.selectByPrimaryKey(category.getRootid()).getRoot());

        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andCategoryNotEqualTo(categoryMapper.selectByPrimaryKey(category.getId()).getCategory()).andCategoryEqualTo(category.getCategory());
        if (categoryMapper.selectByExample(categoryExample).size() > 0) {
            return null;
        }
        category.setUpdatetime(new Date());

        int num = categoryMapper.updateByPrimaryKeySelective(category);

        category.setSuperior(rootMapper.selectByPrimaryKey(category.getRootid()).getRoot());

        return num > 0 ? category : null;
    }

    @Override
    public int deleteCategory(int id) {
        LabelExample labelExample = new LabelExample();
        labelExample.createCriteria().andCategoryidLike("%," + id + ",%");
        return labelMapper.countByExample(labelExample) == 0 ? categoryMapper.deleteByPrimaryKey(id) : -1;
    }

}
