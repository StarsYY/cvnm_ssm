package cn.icylee.controller.back;

import cn.icylee.service.back.DashboardService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/adm/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @ResponseBody
    @RequestMapping(value = "order", method = RequestMethod.GET)
    public Map<String, Object> getDashboardOrder() {
        return ResponseData.success(dashboardService.getDashboardOrder(), "首页订单展示");
    }

    @ResponseBody
    @RequestMapping(value = "panel", method = RequestMethod.GET)
    public Map<String, Object> getPanel() {
        Map<String, Object> map = new HashMap<>();
        map.put("user", dashboardService.getUserCount());
        map.put("article", dashboardService.getArticleCount());
        map.put("money", dashboardService.getMoney());
        map.put("course", dashboardService.getCourseCount());
        return ResponseData.success(map, "面板");
    }

    @ResponseBody
    @RequestMapping(value = "chart", method = RequestMethod.GET)
    public Map<String, Object> getChart() throws ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("user", dashboardService.getUserChart());
        map.put("article", dashboardService.getArticleChart());
        map.put("order", dashboardService.getOrderChart());
        map.put("course", dashboardService.getCourseChart());
        return ResponseData.success(map, "图标");
    }

}
