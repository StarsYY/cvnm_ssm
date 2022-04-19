package cn.icylee.service.back;

import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Report;

import java.util.List;

public interface ReportService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getReportTotal(TableParameter tableParameter);

    List<Report> getPageReport(TableParameter tableParameter);

    int deleteReport(int id);
    
}
