package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.GrowService;
import cn.icylee.service.front.IndexService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    UserMapper userMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    GrowService growService;

    @Autowired
    RotationMapper rotationMapper;

    @Autowired
    SignMapper signMapper;

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
                pp.setIcon(plate1.getIcon());
                labelTrees.add(pp);
            }
            p.setChildren(labelTrees);
            optionPlate.add(p);
        }
        return optionPlate;
    }

    @Override
    public List<Article> getArticle(Index index) {
        List<Article> articleList = null;
        if (index.getDefOrTree() == 1 && index.getLeftId() == 2) {
            if (index.getContentTag() == 1) {
                articleList = articleMapper.getMostHotArticle(index);
            } else {
                index.setContentTag(index.getContentTag() - 1);
                articleList = articleMapper.getIndexArticle(index);
            }
        } else if (index.getDefOrTree() == 1 && index.getLeftId() == 4) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(index.getUsername());
            int uid = userMapper.selectByExample(userExample).get(0).getUid();

            if (index.getContentTag() == 1) {
                FollowExample followExample = new FollowExample();
                followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("user");

                if (followMapper.countByExample(followExample) > 0) {
                    StringBuilder ids = new StringBuilder();
                    for (Follow follow : followMapper.selectByExample(followExample)) {
                        ids.append(follow.getDataid()).append(',');
                    }
                    index.setIds(ids.substring(0, ids.length() - 1));

                    articleList = articleMapper.getFollowUser(index);
                }
            } else if (index.getContentTag() == 2) {
                FollowExample followExample = new FollowExample();
                followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("plate");

                if (followMapper.countByExample(followExample) > 0) {
                    StringBuilder ids = new StringBuilder();
                    for (Follow follow : followMapper.selectByExample(followExample)) {
                        ids.append(follow.getDataid()).append(',');
                    }
                    index.setIds(ids.substring(0, ids.length() - 1));

                    articleList = articleMapper.getFollowPlate(index);
                }
            } else {
                FollowExample followExample = new FollowExample();
                followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("label");

                if (followMapper.countByExample(followExample) > 0) {
                    StringBuilder ids = new StringBuilder();
                    for (Follow follow : followMapper.selectByExample(followExample)) {
                        ids.append(',').append(follow.getDataid()).append(',').append('|');
                    }
                    index.setIds(ids.substring(0, ids.length() - 1));

                    articleList = articleMapper.getFollowLabel(index);
                }
            }
        } else {
            articleList = articleMapper.getIndexArticle(index);
        }

        if (articleList != null) {
            for (Article article : articleList) {
                article.setGrow(Tool.setLevel(article.getGrow()));

                if (!index.getUsername().equals("") && index.getUsername() != null) {
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andNicknameEqualTo(index.getUsername());
                    int userId = userMapper.selectByExample(userExample).get(0).getUid();

                    PreferExample preferExample = new PreferExample();
                    preferExample.createCriteria().andUseridEqualTo(userId).andDatasourceEqualTo("article")
                            .andDataidEqualTo(article.getId());

                    List<Prefer> preferList = preferMapper.selectByExample(preferExample);
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
            }
        }
        return articleList;
    }

    @Override
    public List<Rotation> getRotation() {
        return rotationMapper.selectByExample(null);
    }

    @Override
    public List<Article> getHotArticleOnRight() {
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andHotEqualTo("Hot").andIsdelEqualTo(0)
                .andStatusEqualTo("已发布").andPublishEqualTo("公开");
        articleExample.setOrderByClause(" createtime desc limit 6");
        return articleMapper.selectByExample(articleExample);
    }

    @Override
    public List<Label> getHotLabelOnRight() {
        List<Label> labelList = labelMapper.selectByExample(null);

        for (Label label : labelList) {
            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andLabelidLike("%," + label.getId() + ",%");
            label.setArticleCount(articleMapper.countByExample(articleExample));
        }

        labelList = labelList.stream().sorted(Comparator.comparing(Label::getArticleCount).reversed())
                .collect(Collectors.toList()).subList(0, 10);

        return labelList;
    }

    @Override
    public List<Article> getRecommendArticleOnRight() {
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andTagEqualTo("推荐").andIsdelEqualTo(0)
                .andStatusEqualTo("已发布").andPublishEqualTo("公开");
        articleExample.setOrderByClause(" createtime desc limit 6");
        return articleMapper.selectByExample(articleExample);
    }

    @Override
    public Plate getPlateById(Plate plate) throws ParseException {
        Plate p = plateMapper.getIndexPlate(plate.getId());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH ) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String START = year + "-" + month + "-" + day + " 00:00:00";
        String FINAL = year + "-" + month + "-" + day + " 23:59:59";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date dateStart = format.parse(START);
        Date dateFinal = format.parse(FINAL);

        ArticleExample articleExample = new ArticleExample();
        ArticleExample.Criteria criteria = articleExample.createCriteria();
        criteria.andPlateidEqualTo(plate.getId()).andStatusEqualTo("已发布").andIsdelEqualTo(0);
        List<Article> articleList = articleMapper.selectByExample(articleExample);

        int commentCount = 0;
        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId()).andStatusEqualTo(1);
            commentCount += commentMapper.countByExample(commentExample);
        }
        p.setReplyCount(commentCount);

        criteria.andCreatetimeBetween(dateStart, dateFinal);
        p.setTodayArticle(articleMapper.countByExample(articleExample));

        if (plate.getUsername() != null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(plate.getUsername());
            int uid = userMapper.selectByExample(userExample).get(0).getUid();

            FollowExample followExample = new FollowExample();
            followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("plate").andDataidEqualTo(plate.getId());
            p.setFollow(followMapper.countByExample(followExample) != 0);
        }

        return p;
    }

    @Override
    public List<Plate> getPlateChildren(int id) {
        PlateExample plateExample = new PlateExample();
        plateExample.createCriteria().andAncestorEqualTo(id);

        if (plateMapper.countByExample(plateExample) > 0) {
            List<Plate> plateList = plateMapper.selectByExample(plateExample);

            for (Plate plate : plateList) {
                ArticleExample articleExample = new ArticleExample();
                articleExample.createCriteria().andPlateidEqualTo(plate.getId()).andStatusEqualTo("已发布").andIsdelEqualTo(0);
                plate.setArticleCount(articleMapper.countByExample(articleExample));

                int commentCount = 0;
                for (Article article : articleMapper.selectByExample(articleExample)) {
                    CommentExample commentExample = new CommentExample();
                    commentExample.createCriteria().andArticleidEqualTo(article.getId()).andStatusEqualTo(1);
                    commentCount += commentMapper.countByExample(commentExample);
                }
                plate.setReplyCount(commentCount);
            }

            return plateList;
        }

        return null;
    }

    @Override
    public int saveFollowByPlateId(Plate plate) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(plate.getUsername());
        if (userMapper.countByExample(userExample) == 0) {
            return -2;
        }
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        FollowExample followExample = new FollowExample();
        followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("plate").andDataidEqualTo(plate.getId());
        if (followMapper.countByExample(followExample) == 0) {
            Follow follow = new Follow();
            follow.setUserid(uid);
            follow.setDatasource("plate");
            follow.setDataid(plate.getId());
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
    public int saveSign(String username) throws ParseException {
        if (!getSignInStatus(username)) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(username);
            int uid = userMapper.selectByExample(userExample).get(0).getUid();

            Sign sign = new Sign();
            sign.setUserid(uid);
            sign.setCreatetime(new Date());

            if (signMapper.insert(sign) > 0) {
                return growService.updateIncreaseIntegralAndGrowFromSign(uid);
            }
        } else {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean getSignInStatus(String username) throws ParseException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH ) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String START = year + "-" + month + "-" + day + " 00:00:00";
        String FINAL = year + "-" + month + "-" + day + " 23:59:59";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date dateStart = format.parse(START);
        Date dateFinal = format.parse(FINAL);

        SignExample signExample = new SignExample();
        signExample.createCriteria().andUseridEqualTo(uid).andCreatetimeBetween(dateStart, dateFinal);

        return signMapper.countByExample(signExample) > 0;
    }

}
