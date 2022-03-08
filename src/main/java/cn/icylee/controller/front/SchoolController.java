package cn.icylee.controller.front;

import cn.icylee.service.front.SchoolService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    SchoolService schoolService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> getHotModular() {
        Map<String, Object> map = new HashMap<>();
        map.put("courseNav", schoolService.getHotModular());
        map.put("leftNav", schoolService.getLeftNav());
        return ResponseData.success(map, "Nav");
    }

    @ResponseBody
    @RequestMapping(value = "hot", method = RequestMethod.GET)
    public Map<String, Object> getHotCourse(int id) {
        return ResponseData.success(schoolService.getHotCourse(id), "热门文章");
    }

    @ResponseBody
    @RequestMapping(value = "new", method = RequestMethod.GET)
    public Map<String, Object> getNewCourse(int id) {
        return ResponseData.success(schoolService.getNewCourse(id), "最新文章");
    }

    @ResponseBody
    @RequestMapping(value = "dev", method = RequestMethod.GET)
    public Map<String, Object> getDeveloperStory() {
        return ResponseData.success(schoolService.getDeveloperStory(), "开发者故事");
    }

}
