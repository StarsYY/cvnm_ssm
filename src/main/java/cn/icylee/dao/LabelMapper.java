package cn.icylee.dao;

import cn.icylee.bean.Label;
import cn.icylee.bean.LabelExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface LabelMapper {
    int countByExample(LabelExample example);

    int deleteByExample(LabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Label record);

    int insertSelective(Label record);

    List<Label> selectByExample(LabelExample example);

    Label selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Label record, @Param("example") LabelExample example);

    int updateByExample(@Param("record") Label record, @Param("example") LabelExample example);

    int updateByPrimaryKeySelective(Label record);

    int updateByPrimaryKey(Label record);

    List<Label> getLabelForArticle(TableParameter tableParameter);

    int getLabelTotal(TableParameter tableParameter);

    List<Label> getLabelList(TableParameter tableParameter);

    String[] getLabelIds(Label label);
}
