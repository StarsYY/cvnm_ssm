package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.ArticleMapper;
import cn.icylee.dao.CategoryMapper;
import cn.icylee.dao.LabelMapper;
import cn.icylee.dao.PlateMapper;
import cn.icylee.service.front.IndexService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    PlateMapper plateMapper;

    @Override
    public List<LabelTree> getPlateTree() {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.createCriteria();
        criteria.andAncestorEqualTo(0);
        List<Plate> plateList = plateMapper.selectByExample(plateExample);
        List<LabelTree> optionPlate = new ArrayList<>();
        for (Plate plate : plateList) {
            LabelTree p = new LabelTree();
            p.setValue(plate.getId());
            p.setLabel(plate.getPlate());
            PlateExample example = new PlateExample();
            PlateExample.Criteria exampleCriteria = example.createCriteria();
            exampleCriteria.andAncestorEqualTo(plate.getId());
            List<Plate> plates = plateMapper.selectByExample(example);
            List<LabelTree> labelTrees = new ArrayList<>();
            for (Plate plate1 : plates) {
                LabelTree pp = new LabelTree();
                pp.setValue(plate1.getId());
                pp.setLabel(plate1.getPlate());
                pp.setIs("children");
                labelTrees.add(pp);
            }
            p.setChildren(labelTrees);
            optionPlate.add(p);
        }
        return optionPlate;
    }

    @Override
    public List<Article> getArticle(Index index) {
        List<Article> articleList = new ArrayList<>();
        if (index.getDefOrTree() == 1) { // 首页左侧上面 3 个标签
            if (index.getLeftId() == 1) { // 所有帖子
                if (index.getContentTag() == 1) { // 最新发布
                    articleList = articleMapper.getNewArticle(index);
                } else if (index.getContentTag() == 2) { // 最新回复
                    articleList = articleMapper.getNewCommentArticle(index);
                } else if (index.getContentTag() == 3) { // 最多回复
                    articleList = articleMapper.getMostCommentArticle(index);
                }
            }
        }
        for (Article article : articleList) {
            article.setGrow(Tool.setLevel(article.getGrow()));
        }
        return articleList;
    }

}
