package cn.icylee.controller.back;

import cn.icylee.bean.Report;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.ReportService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adm/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllReport(TableParameter tableParameter) {
        tableParameter = reportService.setIdsTool(tableParameter);
        int total = reportService.getReportTotal(tableParameter);
        List<Report> reportList = reportService.getPageReport(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", reportList);
        return ResponseData.success(map, "举报列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteReport(@RequestBody Report report) {
        return reportService.deleteReport(report.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
