package cn.icylee.dao;

import cn.icylee.bean.*;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ModularMapper {
    int countByExample(ModularExample example);

    int deleteByExample(ModularExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Modular record);

    int insertSelective(Modular record);

    List<Modular> selectByExample(ModularExample example);

    Modular selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Modular record, @Param("example") ModularExample example);

    int updateByExample(@Param("record") Modular record, @Param("example") ModularExample example);

    int updateByPrimaryKeySelective(Modular record);

    int updateByPrimaryKey(Modular record);

    List<Modular> getModularList(TableParameter tableParameter);

    List<Modular> getHotModular();
}