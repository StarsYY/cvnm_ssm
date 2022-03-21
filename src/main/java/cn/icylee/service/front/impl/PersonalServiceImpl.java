package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.PersonalService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    PlateMapper plateMapper;

    @Autowired
    VerifyMapper verifyMapper;

    @Autowired
    IntegralMapper integralMapper;

    private User getUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0);
    }

    @Override
    public User getUser(String username, String loginName) {
        User user = getUserByUsername(username);
        user.setGrow(Tool.setLevel(user.getGrow()));

        FollowExample feFans = new FollowExample();
        feFans.createCriteria().andDataidEqualTo(user.getUid()).andDatasourceEqualTo("user");

        user.setFans(followMapper.countByExample(feFans));

        FollowExample feFollow = new FollowExample();
        feFollow.createCriteria().andUseridEqualTo(user.getUid()).andDatasourceEqualTo("user");
        user.setAttention(followMapper.countByExample(feFollow));

        FollowExample FE = new FollowExample();
        FE.createCriteria().andUseridEqualTo(getUserByUsername(loginName).getUid()).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
        user.setFollow(followMapper.countByExample(FE) != 0);

        user.setPassword("");

        return user;
    }

    @Override
    public Map<String, Integer> getCommunication(String username) {
        Map<String, Integer> communication = new HashMap<>();
        int id = getUserByUsername(username).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id);
        communication.put("文章", articleMapper.countByExample(articleExample));

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andUseridEqualTo(id);
        communication.put("回复", commentMapper.countByExample(commentExample));

        communication.put("获赞", userMapper.getArticleUp(id) + userMapper.getCommentUp(id));

        return communication;
    }

    @Override
    public List<Article> getNewArticle(String username) {
        int id = getUserByUsername(username).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id);
        articleExample.setOrderByClause(" createtime desc LIMIT 10");

        List<Article> articleList = articleMapper.selectByExample(articleExample);
        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));
        }

        return articleList;
    }

    @Override
    public List<User> getFollow(String username) {
        List<User> userList = userMapper.getFollowUser(getUserByUsername(username).getUid());
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
        }

        return userList;
    }

    @Override
    public List<User> getFans(String username) {
        List<User> userList = userMapper.getFansUser(getUserByUsername(username).getUid());
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
        }

        return userList;
    }

    @Override
    public List<Article> getCollect(String username) {
        int id = getUserByUsername(username).getUid();

        List<Article> articleList = articleMapper.getCollectArticle(id);
        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));

            FollowExample feCollect = new FollowExample();
            feCollect.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setCollect(followMapper.countByExample(feCollect));

            PreferExample peLike = new PreferExample();
            peLike.createCriteria().andUseridEqualTo(id).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

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

            FollowExample fe = new FollowExample();
            FollowExample.Criteria fc = fe.createCriteria();
            fc.andUseridEqualTo(id).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setStar(followMapper.selectByExample(fe).size() != 0);
        }

        return articleList;
    }

    @Override
    public List<Article> getMyArticle(String username, String loginName) {
        int id = getUserByUsername(username).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id);
        List<Article> articleList = articleMapper.selectByExample(articleExample);

        for (Article article : articleList) {
            article.setContent("");
            article.setPlate(plateMapper.selectByPrimaryKey(article.getPlateid()).getPlate());

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));

            PreferExample peDown = new PreferExample();
            peDown.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(0);
            article.setDown(preferMapper.countByExample(peDown));

            FollowExample feCollect = new FollowExample();
            feCollect.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setCollect(followMapper.countByExample(feCollect));

            PreferExample peLike = new PreferExample();
            peLike.createCriteria().andUseridEqualTo(getUserByUsername(loginName).getUid()).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

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

            FollowExample fe = new FollowExample();
            FollowExample.Criteria fc = fe.createCriteria();
            fc.andUseridEqualTo(getUserByUsername(loginName).getUid()).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setStar(followMapper.selectByExample(fe).size() != 0);
        }

        return articleList;
    }

    private List<User> getMyFansOrFollow(boolean is, String username, String loginName) {
        int id = getUserByUsername(username).getUid();

        List<User> userList;
        if (is) {
            userList = userMapper.getFansUser2(id);
        } else {
            userList = userMapper.getFollowUser2(id);
        }
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
            user.setPassword("");

            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andUseridEqualTo(user.getUid());
            user.setArticle(articleMapper.countByExample(articleExample));

            FollowExample feFollow =  new FollowExample();
            feFollow.createCriteria().andUseridEqualTo(user.getUid()).andDatasourceEqualTo("user");
            user.setAttention(followMapper.countByExample(feFollow));

            FollowExample feFans = new FollowExample();
            feFans.createCriteria().andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
            user.setFans(followMapper.countByExample(feFans));

            FollowExample isFollow = new FollowExample();
            isFollow.createCriteria().andUseridEqualTo(getUserByUsername(loginName).getUid()).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
            user.setFollow(followMapper.countByExample(isFollow) != 0);
        }
        return userList;
    }

    @Override
    public List<User> getMyFans(String username, String loginName) {
        return getMyFansOrFollow(true, username, loginName);
    }

    @Override
    public List<User> getMyFollow(String username, String loginName) {
        return getMyFansOrFollow(false, username, loginName);
    }

    @Override
    public Verify getVerify(String username) {
        int id = getUserByUsername(username).getUid();
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria().andUseridEqualTo(id).andTypeEqualTo(1);
        List<Verify> verifyList = verifyMapper.selectByExample(verifyExample);
        return verifyList.size() > 0 ? verifyList.get(0) : null;
    }

    @Override
    public List<Integral> getIntegral(String username, Integer page) {
        int id = getUserByUsername(username).getUid();
        IntegralExample integralExample = new IntegralExample();
        integralExample.createCriteria().andUseridEqualTo(id);
        page = (page - 1) * 20;
        integralExample.setOrderByClause(" createtime desc limit " + page + ", 10");
        return integralMapper.selectByExample(integralExample);
    }

    @Override
    public Integer getIntegralTotal(String username) {
        int id = getUserByUsername(username).getUid();
        IntegralExample integralExample = new IntegralExample();
        integralExample.createCriteria().andUseridEqualTo(id);
        return integralMapper.countByExample(integralExample);
    }

    @Override
    public List<Article> getMyDraft(String username) {
        int id = getUserByUsername(username).getUid();
        return articleMapper.getMyDraft(id);
    }

    @Override
    public List<Comment> getMyComment(String username) {
        int id = getUserByUsername(username).getUid();
        List<Comment> commentList = commentMapper.getMyReply(id);

        for (Comment comment : commentList) {
            if (comment.getComid() != 0) {
                comment.setCm(commentMapper.getMyReplyRe(comment.getComid()));
            } else {
                comment.setCm(null);
            }
        }
        return commentList;
    }

}
