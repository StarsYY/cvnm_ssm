package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CategoryMapper;
import cn.icylee.dao.LabelMapper;
import cn.icylee.dao.RootMapper;
import cn.icylee.service.back.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import java.lang.String;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    RootMapper rootMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<LabelTree> getCategoryTree() {
        List<LabelTree> Tree = new ArrayList<>();

        LabelTree labelTree = new LabelTree();
        labelTree.setId(0);
        labelTree.setLabel("所有分类");

        List<LabelTree> rootTrees = new ArrayList<>();

        List<Root> rootList = rootMapper.selectByExample(null);

        for (Root root : rootList) {
            LabelTree rootTree = new LabelTree();
            rootTree.setId(root.getId());
            rootTree.setLabel(root.getRoot());
            rootTree.setIs("root");

            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andRootidEqualTo(root.getId());
            List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

            List<LabelTree> labelTrees = new ArrayList<>();

            for (Category category : categoryList) {
                LabelTree tree = new LabelTree();
                tree.setId(category.getId());
                tree.setLabel(category.getCategory());
                tree.setIs("category");
                labelTrees.add(tree);
            }
            rootTree.setChildren(labelTrees);
            rootTrees.add(rootTree);
        }
        labelTree.setChildren(rootTrees);
        Tree.add(labelTree);
        return Tree;
    }

    @Override
    public int getLabelTotal(TableParameter tableParameter) {
        if (tableParameter.getIds() != null && !tableParameter.getIds().equals("")) {
            String[] arr_ids = tableParameter.getIds().substring(0, tableParameter.getIds().length() - 1).split(",");

            StringBuilder ids = new StringBuilder();
            for (String id : arr_ids) {
                ids.append(",").append(id).append(",|");
            }
            tableParameter.setIds(ids.substring(0, ids.length() - 1));
        } else if (tableParameter.getRootid() != null && !tableParameter.getRootid().equals("")) {
            String[] arr_ids = categoryMapper.getCategoryIds(Integer.parseInt(tableParameter.getRootid()));

            StringBuilder ids = new StringBuilder();
            for (String id : arr_ids) {
                ids.append(",").append(id).append(",|");
            }

            if (ids.length() > 0 && !"null".equals(ids.toString()) && !"".equals(ids.toString())) {
                tableParameter.setIds(ids.substring(0, ids.length() - 1));
            } else {
                tableParameter.setIds(",0,");
            }
        }
        return labelMapper.getLabelTotal(tableParameter);
    }

    @Override
    public List<Object> getAllRoot() {
        List<Object> allRoot = new ArrayList<>();
        List<Root> rootList = rootMapper.selectByExample(null);
        for (Root root : rootList) {
            Map<String, Object> K_V = new HashMap<>();
            K_V.put("id", String.valueOf(root.getId()));
            K_V.put("root", root.getRoot());
            String[] list = categoryMapper.getCategoryIds(root.getId());
            K_V.put("children", list);
            allRoot.add(K_V);
        }
        return allRoot;
    }

    @Override
    public Map<String, String> getAllCategory() {
        Map<String, String> allCategory = new HashMap<>();
        List<Category> categoryList = categoryMapper.selectByExample(null);
        for (Category category : categoryList) {
            allCategory.put(String.valueOf(category.getId()), category.getCategory());
        }
        return allCategory;
    }

    @Override
    public Map<String, Object> getPageLabel(TableParameter tableParameter) {
        List<Label> labelList = labelMapper.getLabelList(tableParameter);
        Map<String, Object> allLabel = new HashMap<>();
        for (Label label : labelList) {
            String[] categoryId = label.getCategoryid().substring(1, label.getCategoryid().length() - 1).split(",");
            Map<String, String> map = new HashMap<>();
            for (String value : categoryId) {
                String category = categoryMapper.selectByPrimaryKey(Integer.parseInt(value)).getCategory();
                map.put(value, category);
            }
            label.setCategoryMap(map);
        }
        allLabel.put("labelList", labelList);
        allLabel.put("allCategory", getAllCategory());
        return allLabel;
    }

    @Override
    public int saveLabel(Label label) {
        LabelExample labelExample = new LabelExample();
        LabelExample.Criteria criteria = labelExample.createCriteria();
        criteria.andLabelEqualTo(label.getLabel());
        if (labelMapper.selectByExample(labelExample).size() > 0) {
            return 0;
        }
        label.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        label.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return labelMapper.insert(label);
    }

    @Override
    public Label getLabelById(int id) {
        Label label = labelMapper.selectByPrimaryKey(id);
        label.setCategoryid(label.getCategoryid().substring(1, label.getCategoryid().length() - 1));
        return label;
    }

    @Override
    public int updateLabel(Label label) {
        LabelExample labelExample = new LabelExample();
        LabelExample.Criteria criteria = labelExample.createCriteria();
        criteria.andLabelNotEqualTo(getLabelById(label.getId()).getLabel()).andLabelEqualTo(label.getLabel());
        if (labelMapper.selectByExample(labelExample).size() > 0) {
            return 0;
        }
        label.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return labelMapper.updateByPrimaryKeySelective(label);
    }

    @Override
    public int deleteLabel(int id) {
        return labelMapper.selectByPrimaryKey(id) != null ? labelMapper.deleteByPrimaryKey(id) : 0;
    }

}
