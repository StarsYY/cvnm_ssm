package cn.icylee.dao;

import cn.icylee.bean.Usermedal;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.UsermedalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsermedalMapper {
    int countByExample(UsermedalExample example);

    int deleteByExample(UsermedalExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Usermedal record);

    int insertSelective(Usermedal record);

    List<Usermedal> selectByExample(UsermedalExample example);

    Usermedal selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Usermedal record, @Param("example") UsermedalExample example);

    int updateByExample(@Param("record") Usermedal record, @Param("example") UsermedalExample example);

    int updateByPrimaryKeySelective(Usermedal record);

    int updateByPrimaryKey(Usermedal record);

    int getUsermedalTotal(TableParameter tableParameter);

    List<Usermedal> getUsermedalList(TableParameter tableParameter);
}