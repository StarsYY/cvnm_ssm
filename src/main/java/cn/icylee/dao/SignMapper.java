package cn.icylee.dao;

import cn.icylee.bean.Sign;
import cn.icylee.bean.SignExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface SignMapper {
    int countByExample(SignExample example);

    int deleteByExample(SignExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Sign record);

    int insertSelective(Sign record);

    List<Sign> selectByExample(SignExample example);

    Sign selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Sign record, @Param("example") SignExample example);

    int updateByExample(@Param("record") Sign record, @Param("example") SignExample example);

    int updateByPrimaryKeySelective(Sign record);

    int updateByPrimaryKey(Sign record);

    int getSignTotal(TableParameter tableParameter);

    List<Sign> getSignList(TableParameter tableParameter);
}