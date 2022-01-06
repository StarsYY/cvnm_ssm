package cn.icylee.service.front;

import cn.icylee.bean.Comment;

import java.util.List;
import java.util.Map;

public interface DetailService {

    int getUserId(String username);

    int updateArticleWatch(int id);

    List<Object> getArticleById(Comment comment);

    List<Comment> getAllComment(Comment comment);

    Map<String, Comment> getAllReply(int aid, int cid, Map<String, Comment> CommentList, int reviewId, int recursion, String nickname);

    int saveComment(Comment comment);

    int savePreferUpOrDownArticle(Comment comment);

    int savePreferUpArticleComment(Comment comment);

    int saveFollowStarArticle(Comment comment);

    int saveFollowAuthor(Comment comment);

}
