package cn.icylee.service.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Comment;
import cn.icylee.bean.Medal;
import cn.icylee.bean.Report;

import java.util.List;
import java.util.Map;

public interface DetailService {

    List<Article> getHotArticleByUid(int uid);

    List<Article> getNewArticleByUid(int uid);

    int updateArticleWatch(int id);

    List<Object> getArticleById(Comment comment);

    String getUsernameTool(int articleId);

    int getUidTool(int articleId);

    List<Medal> getUserMedal(int uid);

    List<Comment> saveGetAllComment(Comment comment);

    Map<String, Comment> getAllReply(int aid, int cid, Map<String, Comment> CommentList, int reviewId, int recursion, String nickname);

    int saveComment(Comment comment);

    int savePreferUpOrDownArticle(Comment comment);

    int savePreferUpArticleComment(Comment comment);

    int saveFollowStarArticle(Comment comment);

    int saveFollowAuthor(Comment comment);

    int saveReport(Report report);

}
