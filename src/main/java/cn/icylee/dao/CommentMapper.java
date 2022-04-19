package cn.icylee.dao;

import cn.icylee.bean.Comment;
import cn.icylee.bean.CommentExample;
import java.util.List;

import cn.icylee.bean.Index;
import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper {
    int countByExample(CommentExample example);

    int deleteByExample(CommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    List<Comment> selectByExample(CommentExample example);

    Comment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByExample(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    int getCommentTotal(TableParameter tableParameter);

    List<Comment> getCommentList(TableParameter tableParameter);

    int deleteComment(String ids);

    List<Comment> getMyReply(Index index);

    Comment getMyReplyRe(Integer comid);
}