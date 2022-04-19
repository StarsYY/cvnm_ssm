package cn.icylee.controller.back;

import cn.icylee.bean.Learning;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.LearningService;
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
@RequestMapping("/adm/learning")
public class LearningController {

    @Autowired
    LearningService learningService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllLearning(TableParameter tableParameter) {
        tableParameter = learningService.setIdsTool(tableParameter);
        int total = learningService.getLearningTotal(tableParameter);
        List<Learning> learningList = learningService.getPageLearning(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", learningList);
        return ResponseData.success(map, "学习记录列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteLearning(@RequestBody Learning learning) {
        return learningService.deleteLearning(learning.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
