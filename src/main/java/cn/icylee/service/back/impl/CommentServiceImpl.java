package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.back.CommentService;
import cn.icylee.service.front.GrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GrowService growService;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    MessageMapper messageMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getArticle() != null && !tableParameter.getArticle().equals("")) {
            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andTitleLike("%" + tableParameter.getArticle() + "%");

            List<Article> articleList = articleMapper.selectByExample(articleExample);
            if (articleList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (Article article : articleList) {
                    ids.append(article.getId()).append(",");
                }
                tableParameter.setIds(ids.toString());
            } else {
                tableParameter.setIds(",0,");
            }
        }
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder cids = new StringBuilder();
                for (User user : userList) {
                    cids.append(user.getUid()).append(",");
                }
                tableParameter.setCids(cids.toString());
            } else {
                tableParameter.setCids(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getCommentTotal(TableParameter tableParameter) {
        return commentMapper.getCommentTotal(tableParameter);
    }

    @Override
    public List<Comment> getPageComment(TableParameter tableParameter) {
        List<Comment> commentList = commentMapper.getCommentList(tableParameter);
        for (Comment comment : commentList) {
            comment.setUsername(userMapper.selectByPrimaryKey(comment.getUserid()).getNickname());
            comment.setArticle(articleMapper.selectByPrimaryKey(comment.getArticleid()).getTitle());

            if (comment.getComid() != 0) {
                comment.setReviewName(userMapper.selectByPrimaryKey(commentMapper.selectByPrimaryKey(comment.getComid()).getUserid()).getNickname());
            }
        }
        return commentList;
    }

    @Override
    public int updateStatus(Comment comment) {
        if (comment.getStatus().equals(commentMapper.selectByPrimaryKey(comment.getId()).getStatus())) {
            if (comment.getStatus().equals(1)) {
                comment.setStatus(0);
            } else {
                comment.setStatus(1);

                if (commentMapper.updateByPrimaryKeySelective(comment) > 0) {
                    return growService.updateIncreaseIntegralAndGrowFromCommentOrDiscuss(comment.getUserid()) > 0 ? 2 : 0;
                }
            }
            return commentMapper.updateByPrimaryKeySelective(comment);
        }
        return 0;
    }

    @Override
    public StringBuilder setIds(int id, StringBuilder ids) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andComidEqualTo(id);

        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        for (Comment comment : commentList) {
            ids.append(comment.getId()).append(",");
            setIds(comment.getId(), ids);
        }
        return ids;
    }

    @Override
    public int[] deleteComment(int id) {
        StringBuilder ids = new StringBuilder();
        ids.append(id).append(",").append(setIds(id, ids));
        String Ids = ids.substring(0, ids.length() / 2 - 1);
        int num = commentMapper.deleteComment(Ids);
        if (num >= 0) {
            preferMapper.deletePreferByComment(Ids);
            reportMapper.deleteReportByComment(Ids);
            messageMapper.deleteMessageByComment(Ids);

            MessageExample messageExample = new MessageExample();
            messageExample.createCriteria().andContentEqualTo(String.valueOf(id)).andDatasourceEqualTo("article");
            messageMapper.deleteByExample(messageExample);

            return Arrays.stream(Ids.split(",")).mapToInt(Integer::parseInt).toArray();
        }
        return null;
    }

}
