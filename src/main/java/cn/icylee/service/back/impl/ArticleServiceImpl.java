package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.back.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    RootMapper rootMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    PlateMapper plateMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<LabelTree> getLabelTree() {
        List<LabelTree> rootTrees = new ArrayList<>();

        List<Root> rootList = rootMapper.selectByExample(null);
        for (Root root : rootList) {
            LabelTree rootTree = new LabelTree();
            rootTree.setId(root.getId());
            rootTree.setLabel(root.getRoot());
            rootTree.setIs("root");

            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria exampleCriteria = categoryExample.createCriteria();
            exampleCriteria.andRootidEqualTo(root.getId());
            List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

            List<LabelTree> treeMiddle = new ArrayList<>();

            for (Category category : categoryList) {
                LabelTree labelTreeMiddle = new LabelTree();
                labelTreeMiddle.setId(category.getId());
                labelTreeMiddle.setLabel(category.getCategory());
                labelTreeMiddle.setIs("category");

                LabelExample labelExample = new LabelExample();
                LabelExample.Criteria criteria = labelExample.createCriteria();
                criteria.andCategoryidEqualTo("%," + category.getId() + ",%");
                List<Label> labelList = labelMapper.selectByExample(labelExample);

                List<LabelTree> treeFinal = new ArrayList<>();

                for (Label label : labelList) {
                    LabelTree labelTreeFinal = new LabelTree();
                    labelTreeFinal.setId(label.getId());
                    labelTreeFinal.setLabel(label.getLabel());
                    labelTreeFinal.setIs("label");
                    treeFinal.add(labelTreeFinal);
                }
                labelTreeMiddle.setChildren(treeFinal);
                treeMiddle.add(labelTreeMiddle);
            }
            rootTree.setChildren(treeMiddle);
            rootTrees.add(rootTree);
        }
        return rootTrees;
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
    public List<Object> getAllCategory() {
        List<Object> allCategory = new ArrayList<>();
        List<Category> categoryList = categoryMapper.selectByExample(null);
        for (Category category : categoryList) {
            Map<String, Object> K_V = new HashMap<>();
            K_V.put("id", String.valueOf(category.getId()));
            K_V.put("category", category.getCategory());
            Label label = new Label();
            label.setCategoryid("%," + category.getId() + ",%");
            String[] list = labelMapper.getLabelIds(label);
            K_V.put("children", list);
            allCategory.add(K_V);
        }
        return allCategory;
    }

    @Override
    public Map<String, String> getAllLabel() {
        Map<String, String> allLabel = new HashMap<>();
        List<Label> labelList = labelMapper.selectByExample(null);
        for (Label label : labelList) {
            allLabel.put(String.valueOf(label.getId()), label.getLabel());
        }
        return allLabel;
    }

    @Override
    public Map<String, String> getAllPlate() {
        Map<String, String> allPlate = new HashMap<>();
        List<Plate> plateList = plateMapper.selectByExample(null);
        for (Plate plate : plateList) {
            allPlate.put(String.valueOf(plate.getId()), plate.getPlate());
        }
        return allPlate;
    }

    /**
     * 不好使
     */
    @Override
    public StringBuilder getAllPid(int pid) {
        StringBuilder p = new StringBuilder();
        p.append(pid).append(",");
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andAncestorEqualTo(pid);
        List<Plate> plateList = plateMapper.selectByExample(plateExample);
        for (Plate plate : plateList) {
            getAllPid(plate.getId());
        }
        return p;
    }

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getPlateid() != null && !tableParameter.getPlateid().equals("0") && !tableParameter.getPlateid().equals("")) {
            String s = getAllPid(Integer.parseInt(tableParameter.getPlateid())).substring(0, getAllPid(Integer.parseInt(tableParameter.getPlateid())).length() - 1);
            tableParameter.setPlateid(s);
        }

        if (tableParameter.getIds() != null && !tableParameter.getIds().equals("")) {
            String[] arr_ids = tableParameter.getIds().substring(0, tableParameter.getIds().length() - 1).split(",");

            StringBuilder ids = new StringBuilder();
            for (String id : arr_ids) {
                ids.append(",").append(id).append(",|");
            }
            tableParameter.setIds(ids.substring(0, ids.length() - 1));
        } else {
            if (tableParameter.getCids() != null && !tableParameter.getCids().equals("")) {
                String[] arr_cids = tableParameter.getCids().substring(0, tableParameter.getCids().length() - 1).split(",");

                StringBuilder cids = new StringBuilder();
                for (String cid : arr_cids) {
                    cids.append(",").append(cid).append(",|");
                }
                tableParameter.setCids(cids.substring(0, cids.length() - 1));

                StringBuilder ids = new StringBuilder();
                for (Label label : labelMapper.getLabelForArticle(tableParameter)) {
                    ids.append(",").append(label.getId()).append(",|");
                }

                if (ids.length() > 0 && !"null".equals(ids.toString()) && !"".equals(ids.toString())) {
                    tableParameter.setIds(ids.substring(0, ids.length() - 1));
                } else {
                    tableParameter.setIds(",0,");
                }
            } else if (tableParameter.getRootid() != null && !tableParameter.getRootid().equals("")) {
                String[] arr_cids = categoryMapper.getCategoryIds(Integer.parseInt(tableParameter.getRootid()));

                StringBuilder cids = new StringBuilder();
                for (String id : arr_cids) {
                    cids.append(",").append(id).append(",|");
                }

                if (cids.length() > 0 && !"null".equals(cids.toString()) && !"".equals(cids.toString())) {
                    tableParameter.setCids(cids.substring(0, cids.length() - 1));

                    StringBuilder ids = new StringBuilder();
                    for (Label label : labelMapper.getLabelForArticle(tableParameter)) {
                        ids.append(",").append(label.getId()).append(",|");
                    }

                    if (ids.length() > 0 && !"null".equals(ids.toString()) && !"".equals(ids.toString())) {
                        tableParameter.setIds(ids.substring(0, ids.length() - 1));
                    } else {
                        tableParameter.setIds(",0,");
                    }
                } else {
                    tableParameter.setIds(",0,");
                }
            }
        }
        return tableParameter;
    }

    @Override
    public int getArticleTotal(TableParameter tableParameter) {
        return articleMapper.getArticleTotal(tableParameter);
    }

    @Override
    public Map<String, Object> getPageArticle(TableParameter tableParameter) {
        List<Article> articleList = articleMapper.getArticleList(tableParameter);

        Map<String, Object> allArticle = new HashMap<>();

        for (Article article : articleList) {
            String author = userMapper.selectByPrimaryKey(article.getUserid()).getNickname();

            String[] labelId = article.getLabelid().substring(1, article.getLabelid().length() - 1).split(",");

            Map<String, String> map = new HashMap<>();
            for (String value : labelId) {
                String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
                map.put(value, label);
            }

            article.setPlate(plateMapper.selectByPrimaryKey(article.getPlateid()).getPlate());
            article.setAuthor(author);
            article.setLabelMap(map);
        }

        allArticle.put("articleList", articleList);
        allArticle.put("allCategory", getAllCategory());
        allArticle.put("allLabel", getAllLabel());
        return allArticle;
    }

    @Override
    public Map<String, String> searchUser(String name) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameLike("%" + name + "%");
        Map<String, String> map = new HashMap<>();
        for (User user : userMapper.selectByExample(userExample)) {
            map.put(user.getUid().toString(), user.getNickname());
        }
        return map;
    }

    @Override
    public int saveArticle(Article article) {
        // 前台添加文章用
        if (article.getAuthor() != null && !article.getAuthor().equals("")) {
            UserExample userExample = new UserExample();
            UserExample.Criteria exampleCriteria = userExample.createCriteria();
            exampleCriteria.andNicknameEqualTo(article.getAuthor());
            article.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
            article.setAuthor(null);
        }

        ArticleExample articleExample = new ArticleExample();
        ArticleExample.Criteria criteria = articleExample.createCriteria();
        criteria.andTitleEqualTo(article.getTitle());

        if (articleMapper.selectByExample(articleExample).size() > 0) {
            return 0;
        }

        article.setIsdel(0);

        article.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        article.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return articleMapper.insert(article);
    }

    @Override
    public Article getArticleById(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        article.setAuthor(userMapper.selectByPrimaryKey(article.getUserid()).getNickname());
        article.setLabelid(article.getLabelid().substring(1, article.getLabelid().length() - 1));
        return article;
    }

    @Override
    public int updateArticle(Article article) {
        ArticleExample articleExample = new ArticleExample();
        ArticleExample.Criteria criteria = articleExample.createCriteria();
        criteria.andTitleNotEqualTo(getArticleById(article.getId()).getTitle()).andTitleEqualTo(article.getTitle());
        if (articleMapper.selectByExample(articleExample).size() > 0) {
            return 0;
        }
        article.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int updateStatus(Article article) {
        if (article.getStatus().equals(getArticleById(article.getId()).getStatus())) {
            if (article.getStatus().equals("已发布")) {
                article.setStatus("待审核");
            } else if (article.getStatus().equals("待审核")) {
                article.setStatus("已发布");
            }
            return articleMapper.updateByPrimaryKeySelective(article);
        }
        return 0;
    }

    @Override
    public int updateTag(Article article) {
        if (article.getTag().equals(getArticleById(article.getId()).getTag())) {
            if (article.getTag().equals("精华")) {
                article.setTag("推荐");
            } else {
                article.setTag("精华");
            }
            return articleMapper.updateByPrimaryKeySelective(article);
        }
        return 0;
    }

    @Override
    public int updateRTag(Article article) {
        if (article.getTag().equals(getArticleById(article.getId()).getTag())) {
            if (article.getTag().equals("")) {
                article.setTag("推荐");
            } else {
                article.setTag("");
            }
            return articleMapper.updateByPrimaryKeySelective(article);
        }
        return 0;
    }

    @Override
    public int deleteArticle(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article.getIsdel() == 1) {
            article.setIsdel(0);
        } else {
            article.setIsdel(1);
        }
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int deleteArticleR(int id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

}
