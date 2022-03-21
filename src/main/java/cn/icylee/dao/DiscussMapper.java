package cn.icylee.dao;

import cn.icylee.bean.Discuss;
import cn.icylee.bean.DiscussExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface DiscussMapper {
    int countByExample(DiscussExample example);

    int deleteByExample(DiscussExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Discuss record);

    int insertSelective(Discuss record);

    List<Discuss> selectByExample(DiscussExample example);

    Discuss selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Discuss record, @Param("example") DiscussExample example);

    int updateByExample(@Param("record") Discuss record, @Param("example") DiscussExample example);

    int updateByPrimaryKeySelective(Discuss record);

    int updateByPrimaryKey(Discuss record);

    List<Discuss> getDiscuss(Discuss discuss);

    Double getAVGCourse(Integer id);

    int getDiscussTotal(TableParameter tableParameter);

    List<Discuss> getDiscussList(TableParameter tableParameter);

    int deleteDiscuss(String ids);
}