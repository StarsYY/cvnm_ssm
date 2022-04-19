package cn.icylee.dao;

import cn.icylee.bean.Rotations;
import cn.icylee.bean.RotationsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RotationsMapper {
    int countByExample(RotationsExample example);

    int deleteByExample(RotationsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Rotations record);

    int insertSelective(Rotations record);

    List<Rotations> selectByExample(RotationsExample example);

    Rotations selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Rotations record, @Param("example") RotationsExample example);

    int updateByExample(@Param("record") Rotations record, @Param("example") RotationsExample example);

    int updateByPrimaryKeySelective(Rotations record);

    int updateByPrimaryKey(Rotations record);
}