package cn.icylee.dao;

import cn.icylee.bean.Root;
import cn.icylee.bean.RootExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface RootMapper {
    int countByExample(RootExample example);

    int deleteByExample(RootExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Root record);

    int insertSelective(Root record);

    List<Root> selectByExample(RootExample example);

    Root selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Root record, @Param("example") RootExample example);

    int updateByExample(@Param("record") Root record, @Param("example") RootExample example);

    int updateByPrimaryKeySelective(Root record);

    int updateByPrimaryKey(Root record);

    List<Root> getRootList(TableParameter tableParameter);
}
