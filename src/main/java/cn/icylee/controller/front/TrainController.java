package cn.icylee.controller.front;

import cn.icylee.bean.Modular;
import cn.icylee.service.front.TrainService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
public class TrainController {

    @Autowired
    TrainService trainService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> getBusinessArea() {
        return ResponseData.success(trainService.getBusinessArea(), "BA");
    }

    @ResponseBody
    @RequestMapping(value = "modularAndCourse", method = RequestMethod.GET)
    public Map<String, Object> getModularAndCourse(Modular modular) {
        Map<String, Object> map = new HashMap<>();
        map.put("module", trainService.getBusinessModule(modular.getAncestor()));
        map.put("course", trainService.getCourseList(modular));
        map.put("total", trainService.getAllCourseTotal(modular));
        return ResponseData.success(map, "BM and course");
    }

}
