package cn.icylee.service.front;

import cn.icylee.bean.Comment;

import java.util.List;
import java.util.Map;

public interface DetailService {

    List<Object> getArticleById(int id);

    List<Comment> getAllComment(int id);

    Map<String, Comment> getAllReply(int aid, int cid, Map<String, Comment> CommentList, int reviewId, int recursion);

    int saveComment(Comment comment);

}
