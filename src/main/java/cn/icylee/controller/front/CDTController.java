package cn.icylee.controller.front;

import cn.icylee.bean.Discuss;
import cn.icylee.service.front.CDTService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/course")
public class CDTController {

    @Autowired
    CDTService cdtService;

    @ResponseBody
    @RequestMapping(value = "video", method = RequestMethod.GET)
    public Map<String, Object> showCourse(Discuss discuss) {
        int num = cdtService.updateCourse(discuss.getCourseid());
        return num > 0 ? ResponseData.success(cdtService.getCourse(discuss), "course") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public Map<String, Object> getUser(int uid) {
        return ResponseData.success(cdtService.getUser(uid), "作者");
    }

    @ResponseBody
    @RequestMapping(value = "bool", method = RequestMethod.GET)
    public Map<String, Object> boolDiscuss(Discuss discuss) {
        return ResponseData.success(cdtService.boolDiscuss(discuss), "bool");
    }

    @ResponseBody
    @RequestMapping(value = "discuss/add", method = RequestMethod.POST)
    public Map<String, Object> saveDiscuss(@RequestBody Discuss discuss) {
        int num = cdtService.saveDiscuss(discuss);
        if (num == 1) {
            return ResponseData.success("success", "评论成功");
        } else if (num == 2) {
            return ResponseData.error("您已经评论过了");
        } else {
            return ResponseData.error("网络故障");
        }
    }

    @ResponseBody
    @RequestMapping(value = "discuss", method = RequestMethod.GET)
    public Map<String, Object> getDiscuss(Discuss discuss) {
        return ResponseData.success(cdtService.getDiscuss(discuss), "评论");
    }

    @ResponseBody
    @RequestMapping(value = "discuss/follow", method = RequestMethod.POST)
    public Map<String, Object> saveFollow(@RequestBody Discuss discuss) {
        return cdtService.saveFollow(discuss) > 0 ? ResponseData.success("success", "收藏成功") : ResponseData.error("网络故障");
    }

}
