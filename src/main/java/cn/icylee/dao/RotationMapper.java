package cn.icylee.dao;

import cn.icylee.bean.Rotation;
import cn.icylee.bean.RotationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RotationMapper {
    int countByExample(RotationExample example);

    int deleteByExample(RotationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Rotation record);

    int insertSelective(Rotation record);

    List<Rotation> selectByExample(RotationExample example);

    Rotation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Rotation record, @Param("example") RotationExample example);

    int updateByExample(@Param("record") Rotation record, @Param("example") RotationExample example);

    int updateByPrimaryKeySelective(Rotation record);

    int updateByPrimaryKey(Rotation record);
}