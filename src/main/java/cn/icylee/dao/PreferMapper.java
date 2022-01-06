package cn.icylee.dao;

import cn.icylee.bean.Prefer;
import cn.icylee.bean.PreferExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PreferMapper {
    int countByExample(PreferExample example);

    int deleteByExample(PreferExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Prefer record);

    int insertSelective(Prefer record);

    List<Prefer> selectByExample(PreferExample example);

    Prefer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Prefer record, @Param("example") PreferExample example);

    int updateByExample(@Param("record") Prefer record, @Param("example") PreferExample example);

    int updateByPrimaryKeySelective(Prefer record);

    int updateByPrimaryKey(Prefer record);
}