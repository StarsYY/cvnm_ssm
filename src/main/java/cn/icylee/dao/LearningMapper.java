package cn.icylee.dao;

import cn.icylee.bean.Learning;
import cn.icylee.bean.LearningExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface LearningMapper {
    int countByExample(LearningExample example);

    int deleteByExample(LearningExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Learning record);

    int insertSelective(Learning record);

    List<Learning> selectByExample(LearningExample example);

    Learning selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Learning record, @Param("example") LearningExample example);

    int updateByExample(@Param("record") Learning record, @Param("example") LearningExample example);

    int updateByPrimaryKeySelective(Learning record);

    int updateByPrimaryKey(Learning record);

    List<Learning> getLearningList(TableParameter tableParameter);

    int getLearningTotal(TableParameter tableParameter);
}