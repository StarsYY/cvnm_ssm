package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.DetailService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetailServiceImpl implements DetailService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    PlateMapper plateMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    FollowMapper followMapper;

    @Override
    public int getUserId(String username) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0).getUid();
    }

    @Override
    public int updateArticleWatch(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        article.setWatch(article.getWatch() + 1);
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public List<Object> getArticleById(Comment comment) {
        Article article = articleMapper.selectByPrimaryKey(comment.getArticleid());

        article.setAuthor(userMapper.selectByPrimaryKey(article.getUserid()).getNickname());
        article.setPlate(plateMapper.selectByPrimaryKey(article.getPlateid()).getPlate());

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andArticleidEqualTo(article.getId());
        article.setComment(commentMapper.countByExample(commentExample));

        PreferExample peUp = new PreferExample();
        peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid()).andPushEqualTo(1);
        article.setUp(preferMapper.countByExample(peUp));

        PreferExample peDown = new PreferExample();
        peDown.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid()).andPushEqualTo(0);
        article.setDown(preferMapper.countByExample(peDown));

        FollowExample feCollect = new FollowExample();
        feCollect.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid());
        article.setCollect(followMapper.countByExample(feCollect));

        if (!comment.getUsername().equals("") && comment.getUsername() != null) {
            int userId = getUserId(comment.getUsername());

            PreferExample peLike = new PreferExample();
            peLike.createCriteria().andUseridEqualTo(userId).andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid());

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
            fc.andUseridEqualTo(userId).andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid());
            article.setStar(followMapper.selectByExample(fe).size() != 0);
        }

        String[] labelId = article.getLabelid().substring(1, article.getLabelid().length() - 1).split(",");
        Map<String, String> map = new HashMap<>();
        for (String value : labelId) {
            String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
            map.put(value, label);
        }
        article.setLabelMap(map);

        User user = userMapper.selectByPrimaryKey(article.getUserid());

        user.setGrow(Tool.setLevel(user.getGrow()));

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(user.getUid());
        user.setArticle(articleMapper.countByExample(articleExample));

        FollowExample followExample = new FollowExample();
        followExample.createCriteria().andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
        user.setFans(followMapper.countByExample(followExample));

        CommentExample ce = new CommentExample();
        ce.createCriteria().andUseridEqualTo(user.getUid());
        user.setCount(commentMapper.countByExample(ce));

        if (!comment.getUsername().equals("") && comment.getUsername() != null) {
            int userId = getUserId(comment.getUsername());

            FollowExample fee = new FollowExample();
            FollowExample.Criteria fcc = fee.createCriteria();
            fcc.andUseridEqualTo(userId).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
            user.setFollow(followMapper.selectByExample(fee).size() != 0);
        }

        List<Object> list = new ArrayList<>();
        list.add(article);
        list.add(user);

        return list;
    }

    @Override
    public List<Comment> getAllComment(Comment comment) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andArticleidEqualTo(comment.getArticleid()).andComidEqualTo(0);

        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        for (Comment cm : commentList) {
            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("comment").andDataidEqualTo(cm.getId()).andPushEqualTo(1);
            cm.setUp(preferMapper.countByExample(peUp));

            if (!comment.getUsername().equals("") && comment.getUsername() != null) {
                PreferExample peLike = new PreferExample();
                peLike.createCriteria().andUseridEqualTo(getUserId(comment.getUsername())).andDatasourceEqualTo("comment").andDataidEqualTo(cm.getId()).andPushEqualTo(1);
                cm.setLike(preferMapper.selectByExample(peLike).size() != 0);
            }

            Map<String, Comment> CommentList = new HashMap<>();

            User user = userMapper.selectByPrimaryKey(cm.getUserid());

            cm.setGrow(Tool.setLevel(user.getGrow()));

            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andUseridEqualTo(user.getUid());
            user.setArticle(articleMapper.countByExample(articleExample));

            CommentExample ce = new CommentExample();
            ce.createCriteria().andUseridEqualTo(user.getUid());
            user.setCount(commentMapper.countByExample(ce));

            if (!comment.getUsername().equals("") && comment.getUsername() != null) {
                FollowExample fee = new FollowExample();
                FollowExample.Criteria fcc = fee.createCriteria();
                fcc.andUseridEqualTo(getUserId(comment.getUsername())).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
                user.setFollow(followMapper.selectByExample(fee).size() != 0);

                cm.setCommentList(getAllReply(comment.getArticleid(), cm.getId(), CommentList, 0, 1, comment.getUsername()));
            } else {
                cm.setCommentList(getAllReply(comment.getArticleid(), cm.getId(), CommentList, 0, 1, ""));
            }

            cm.setUser(user);
        }

        return commentList;
    }

    @Override
    public Map<String, Comment> getAllReply(int aid, int cid, Map<String, Comment> CommentList, int reviewId, int recursion, String nickname) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andArticleidEqualTo(aid).andComidEqualTo(cid);

        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        if (commentList.size() > 0) {
            for (Comment comment : commentList) {
                PreferExample peUp = new PreferExample();
                peUp.createCriteria().andDatasourceEqualTo("comment").andDataidEqualTo(comment.getId()).andPushEqualTo(1);
                comment.setUp(preferMapper.countByExample(peUp));

                if (!nickname.equals("")) {
                    PreferExample peLike = new PreferExample();
                    peLike.createCriteria().andUseridEqualTo(getUserId(nickname)).andDatasourceEqualTo("comment").andDataidEqualTo(comment.getId()).andPushEqualTo(1);
                    comment.setLike(preferMapper.selectByExample(peLike).size() != 0);
                }

                if (recursion > 1 && reviewId > 0) {
                    comment.setReviewName(userMapper.selectByPrimaryKey(reviewId).getNickname());
                }

                comment.setUsername(userMapper.selectByPrimaryKey(comment.getUserid()).getNickname());
                comment.setAvatar(userMapper.selectByPrimaryKey(comment.getUserid()).getPortrait());
                CommentList.put(comment.getId().toString(), comment);

                getAllReply(aid, comment.getId(), CommentList, comment.getUserid(), recursion + 1, nickname);
            }
        }

        return CommentList;
    }

    @Override
    public int saveComment(Comment comment) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(comment.getUsername());
        comment.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
        comment.setStatus(0);
        comment.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return commentMapper.insert(comment);
    }

    @Override
    public int savePreferUpOrDownArticle(Comment comment) {
        int userId = getUserId(comment.getUsername());

        PreferExample preferExample = new PreferExample();
        PreferExample.Criteria criteriaLike = preferExample.createCriteria();
        criteriaLike.andUseridEqualTo(userId).andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid());

        List<Prefer> preferList = preferMapper.selectByExample(preferExample);
        if (preferList.size() == 0) {
            Prefer prefer = new Prefer();
            prefer.setUserid(userId);
            prefer.setDatasource("article");
            prefer.setDataid(comment.getArticleid());
            prefer.setPush(comment.getIs());
            prefer.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return preferMapper.insert(prefer);
        } else {
            return preferMapper.deleteByPrimaryKey(preferList.get(0).getId());
        }
    }

    @Override
    public int savePreferUpArticleComment(Comment comment) {
        int userId = getUserId(comment.getUsername());

        PreferExample preferExample = new PreferExample();
        PreferExample.Criteria criteriaLike = preferExample.createCriteria();
        criteriaLike.andUseridEqualTo(userId).andDatasourceEqualTo("comment").andDataidEqualTo(comment.getComid());

        List<Prefer> preferList = preferMapper.selectByExample(preferExample);
        if (preferList.size() == 0) {
            Prefer prefer = new Prefer();
            prefer.setUserid(userId);
            prefer.setDatasource("comment");
            prefer.setDataid(comment.getComid());
            prefer.setPush(comment.getIs());
            prefer.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return preferMapper.insert(prefer);
        } else {
            return preferMapper.deleteByPrimaryKey(preferList.get(0).getId());
        }
    }

    @Override
    public int saveFollowStarArticle(Comment comment) {
        int userId = getUserId(comment.getUsername());

        FollowExample followExample = new FollowExample();
        FollowExample.Criteria criteriaFollow = followExample.createCriteria();
        criteriaFollow.andUseridEqualTo(userId).andDatasourceEqualTo("article").andDataidEqualTo(comment.getArticleid());

        List<Follow> followList = followMapper.selectByExample(followExample);
        if (followList.size() == 0) {
            Follow follow = new Follow();
            follow.setUserid(userId);
            follow.setDatasource("article");
            follow.setDataid(comment.getArticleid());
            follow.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return followMapper.insert(follow);
        } else {
            return followMapper.deleteByPrimaryKey(followList.get(0).getId());
        }
    }

    @Override
    public int saveFollowAuthor(Comment comment) {
        int userId = getUserId(comment.getUsername());

        FollowExample followExample = new FollowExample();
        FollowExample.Criteria criteriaFollow = followExample.createCriteria();
        criteriaFollow.andUseridEqualTo(userId).andDatasourceEqualTo("user").andDataidEqualTo(comment.getUserid());

        List<Follow> followList = followMapper.selectByExample(followExample);
        if (followList.size() == 0) {
            Follow follow = new Follow();
            follow.setUserid(userId);
            follow.setDatasource("user");
            follow.setDataid(comment.getUserid());
            follow.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return followMapper.insert(follow);
        } else {
            return followMapper.deleteByPrimaryKey(followList.get(0).getId());
        }
    }

}
