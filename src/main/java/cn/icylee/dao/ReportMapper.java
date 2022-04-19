package cn.icylee.dao;

import cn.icylee.bean.Report;
import cn.icylee.bean.ReportExample;
import java.util.List;

import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface ReportMapper {
    int countByExample(ReportExample example);

    int deleteByExample(ReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Report record);

    int insertSelective(Report record);

    List<Report> selectByExample(ReportExample example);

    Report selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Report record, @Param("example") ReportExample example);

    int updateByExample(@Param("record") Report record, @Param("example") ReportExample example);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);

    int getReportTotal(TableParameter tableParameter);

    List<Report> getReportList(TableParameter tableParameter);

    int deleteReportByComment(String ids);
}