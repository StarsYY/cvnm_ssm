package cn.icylee.service.back;

import cn.icylee.bean.Comment;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface CommentService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getCommentTotal(TableParameter tableParameter);

    List<Comment> getPageComment(TableParameter tableParameter);

    int updateStatus(Comment comment);

    StringBuilder setIds(int id, StringBuilder ids);

    int[] deleteComment(int id);

}
