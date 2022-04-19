package cn.icylee.dao;

import cn.icylee.bean.Medal;
import cn.icylee.bean.MedalExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface MedalMapper {
    int countByExample(MedalExample example);

    int deleteByExample(MedalExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Medal record);

    int insertSelective(Medal record);

    List<Medal> selectByExample(MedalExample example);

    Medal selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Medal record, @Param("example") MedalExample example);

    int updateByExample(@Param("record") Medal record, @Param("example") MedalExample example);

    int updateByPrimaryKeySelective(Medal record);

    int updateByPrimaryKey(Medal record);

    List<Medal> getMedalList(TableParameter tableParameter);

    List<Medal> getUserMedal(int uid);
}