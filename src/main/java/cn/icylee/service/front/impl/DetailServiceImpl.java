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

    @Override
    public List<Object> getArticleById(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        article.setAuthor(userMapper.selectByPrimaryKey(article.getUserid()).getNickname());
        article.setPlate(plateMapper.selectByPrimaryKey(article.getPlateid()).getPlate());
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andArticleidEqualTo(article.getId());
        article.setComment(commentMapper.countByExample(commentExample));
        String[] labelId = article.getLabelid().substring(1, article.getLabelid().length() - 1).split(",");
        Map<String, String> map = new HashMap<>();
        for (String value : labelId) {
            String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
            map.put(value, label);
        }
        article.setLabelMap(map);
        User user = userMapper.selectByPrimaryKey(article.getUserid());
        user.setGrow(Tool.setLevel(user.getGrow()));
        List<Object> list = new ArrayList<>();
        list.add(article);
        list.add(user);
        return list;
    }

    @Override
    public List<Comment> getAllComment(int id) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andArticleidEqualTo(id).andComidEqualTo(0);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        for (Comment comment : commentList) {
            Map<String, Comment> CommentList = new HashMap<>();
            User user = userMapper.selectByPrimaryKey(comment.getUserid());
            user.setGrow(Tool.setLevel(user.getGrow()));
            comment.setUser(user);
            comment.setCommentList(getAllReply(id, comment.getId(), CommentList, 0, 1));
        }
        return commentList;
    }

    @Override
    public Map<String, Comment> getAllReply(int aid, int cid, Map<String, Comment> CommentList, int reviewId, int recursion) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andArticleidEqualTo(aid).andComidEqualTo(cid);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        if (commentList.size() > 0) {
            for (Comment comment : commentList) {
                if (recursion > 1 && reviewId > 0) {
                    comment.setReviewName(userMapper.selectByPrimaryKey(reviewId).getNickname());
                }
                comment.setUsername(userMapper.selectByPrimaryKey(comment.getUserid()).getNickname());
                comment.setAvatar(userMapper.selectByPrimaryKey(comment.getUserid()).getPortrait());
                CommentList.put(comment.getId().toString(), comment);
                getAllReply(aid, comment.getId(), CommentList, comment.getUserid(), recursion + 1);
            }
        }
        return CommentList;
    }

    @Override
    public int saveComment(Comment comment) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(comment.getUsername());
        comment.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
        comment.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return commentMapper.insert(comment);
    }

}
