package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.GrowService;
import cn.icylee.service.front.TagService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    FollowMapper followMapper;

    @Autowired
    GrowService growService;

    @Override
    public List<Label> getPageLabel(TableParameter tableParameter) throws ParseException {
        List<Label> labelList = labelMapper.getLabelListForTags(tableParameter);

        for (Label label : labelList) {
            ArticleExample articleExample = new ArticleExample();
            ArticleExample.Criteria criteria = articleExample.createCriteria();
            criteria.andLabelidLike("%," + label.getId() + ",%").andStatusEqualTo("已发布").andIsdelEqualTo(0);
            label.setArticleCount(articleMapper.countByExample(articleExample));

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH ) + 1;
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String START = year + "-" + month + "-" + 1 + " 00:00:00";
            String FINAL = year + "-" + month + "-" + lastDay + " 23:59:59";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date dateStart = format.parse(START);
            Date dateFinal = format.parse(FINAL);

            criteria.andCreatetimeBetween(dateStart, dateFinal);
            label.setMonthArticleCount(articleMapper.countByExample(articleExample));
        }

        if (tableParameter.getTitle().equals("hot")) {
            labelList = labelList.stream().sorted(Comparator.comparing(Label::getArticleCount).reversed()).collect(Collectors.toList());
        }

        if (tableParameter.getPage() + tableParameter.getLimit() < labelList.size()) {
            labelList = labelList.subList(tableParameter.getPage(), tableParameter.getPage() + tableParameter.getLimit());
        } else {
            labelList = labelList.subList(tableParameter.getPage(), labelList.size());
        }

        return labelList;
    }

    @Override
    public int labelCount() {
        return labelMapper.countByExample(null);
    }

    @Override
    public Label getLabelById(Label label) {
        Label l = labelMapper.selectByPrimaryKey(label.getId());

        FollowExample followExample = new FollowExample();
        followExample.createCriteria().andDatasourceEqualTo("label").andDataidEqualTo(label.getId());
        l.setFollowCount(followMapper.countByExample(followExample));

        if (label.getUsername() != null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(label.getUsername());
            int uid = userMapper.selectByExample(userExample).get(0).getUid();

            FollowExample fe = new FollowExample();
            fe.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("label").andDataidEqualTo(label.getId());
            l.setFollow(followMapper.countByExample(fe) != 0);
        }
        return l;
    }

    @Override
    public int saveFollowByLabelId(Label label) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(label.getUsername());
        if (userMapper.countByExample(userExample) == 0) {
            return -2;
        }
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        FollowExample followExample = new FollowExample();
        followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("label").andDataidEqualTo(label.getId());
        if (followMapper.countByExample(followExample) == 0) {
            Follow follow = new Follow();
            follow.setUserid(uid);
            follow.setDatasource("label");
            follow.setDataid(label.getId());
            follow.setCreatetime(new Date());

            if (followMapper.insert(follow) > 0) {
                return growService.updateIncreaseIntegralAndGrowFromFollow(uid);
            }
        } else {
            if (followMapper.deleteByPrimaryKey(followMapper.selectByExample(followExample).get(0).getId()) > 0) {
                return growService.updateDecreaseIntegralAndGrowFromFollow(uid);
            }
        }

        return 0;
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

            article.setGrow(Tool.setLevel(article.getGrow()));
        }
        return articleList;
    }

}
