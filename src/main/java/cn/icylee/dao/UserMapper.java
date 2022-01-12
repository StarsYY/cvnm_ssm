package cn.icylee.dao;

import cn.icylee.bean.TableParameter;
import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> getUserList(TableParameter tableParameter);

    int getArticleUp(Integer uid);

    int getCommentUp(Integer id);

    List<User> getFollowUser(Integer uid);

    List<User> getFansUser(Integer uid);

    List<User> getFollowUser2(Integer uid);

    List<User> getFansUser2(Integer uid);
}
