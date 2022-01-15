package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.TagService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TagServiceImpl implements TagService {

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    PlateMapper plateMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<Label> getPageLabel(TableParameter tableParameter) {
        List<Label> labelList = labelMapper.getLabelListForTags(tableParameter);

        for (Label label : labelList) {
            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andLabelidLike("%," + label.getId() + ",%");
            label.setArticleCount(articleMapper.countByExample(articleExample));
        }

        if (tableParameter.getTitle().equals("hot")) {
            labelList = labelList.stream().sorted(Comparator.comparing(Label::getArticleCount).reversed()).collect(Collectors.toList());
        }
        return labelList;
    }

    @Override
    public int labelCount() {
        return labelMapper.countByExample(null);
    }

    @Override
    public Label getLabelById(int id) {
        return labelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Plate> getAllPlate() {
        List<Plate> plateList = plateMapper.selectByExample(null);

        Plate plate = new Plate();
        plate.setId(0);
        plate.setPlate("全部");

        plateList.add(0, plate);
        return plateList;
    }

    @Override
    public int articleCount(TableParameter tableParameter) {
        return articleMapper.getCountTagArticle(tableParameter);
    }

    @Override
    public List<Article> getTagArticle(TableParameter tableParameter) {
        List<Article> articleList = articleMapper.getTagArticle(tableParameter);

        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));

            if (!tableParameter.getNickname().equals("") && tableParameter.getNickname() != null) {
                UserExample userExample = new UserExample();
                UserExample.Criteria criteria = userExample.createCriteria();
                criteria.andNicknameEqualTo(tableParameter.getNickname());
                int userId = userMapper.selectByExample(userExample).get(0).getUid();

                PreferExample peLike = new PreferExample();
                peLike.createCriteria().andUseridEqualTo(userId).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

                List<Prefer> preferList = preferMapper.selectByExample(peLike);
                if (preferList.size() == 0) {
                    article.setLike(false);
                    article.setUnlike(false);
                } else {
                    if (preferList.get(0).getPush() == 1) {
                        article.setLike(true);
                        article.setUnlike(false);
                    } else if (preferList.get(0).getPush() == 0) {
                        article.setLike(false);
                        article.setUnlike(true);
                    }
                }
            }

            User user = userMapper.selectByPrimaryKey(article.getUserid());

            article.setPortrait(user.getPortrait());
            article.setNickname(user.getNickname());
            article.setGrow(Tool.setLevel(user.getGrow()));
        }
        return articleList;
    }

}
