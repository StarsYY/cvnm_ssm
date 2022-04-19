package cn.icylee.controller.front;

import cn.icylee.bean.Discuss;
import cn.icylee.bean.Learning;
import cn.icylee.service.front.CDTService;
import cn.icylee.service.front.SendMessageService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
public class CDTController {

    @Autowired
    CDTService cdtService;

    @Autowired
    SendMessageService sendMessageService;

    @ResponseBody
    @RequestMapping(value = "video", method = RequestMethod.GET)
    public Map<String, Object> showCourse(Discuss discuss) {
        int num = cdtService.updateCourse(discuss.getCourseid());
        Map<String, Object> map = new HashMap<>();
        if (num > 0) {
            map.put("course", cdtService.getCourse(discuss));
            map.put("video", cdtService.getVideoByCourseId(discuss));
            map.put("isAdmin", cdtService.getIsAdmin(discuss.getAuthor()));
            return ResponseData.success(map, "course");
        }
        return ResponseData.error("网络故障");
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
        discuss = cdtService.saveDiscuss(discuss);
        if (discuss.getId() > 0) {
            return sendMessageService.saveMessageFromDiscuss(discuss) > 0
                    ? ResponseData.success("success", "数据修改成功，消息发送成功")
                    : ResponseData.error("数据修改成功，消息发送失败");
        } else if (discuss.getId() == -1) {
            return ResponseData.error("您已经评论过了");
        } else if (discuss.getId() == 0) {
            return ResponseData.success("success", "评论成功");
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

    @ResponseBody
    @RequestMapping(value = "discuss/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteDiscuss(@RequestBody Discuss discuss) {
        return cdtService.deleteDiscuss(discuss.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "learning", method = RequestMethod.POST)
    public Map<String, Object> saveLearning(@RequestBody Learning learning) {
        return cdtService.saveLearning(learning) > 0 ? ResponseData.success("success", "添加学习记录成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "recommend", method = RequestMethod.GET)
    public Map<String, Object> getRecommendById(int id) {
        return ResponseData.success(cdtService.getRecommendById(id), "推荐课程");
    }

}
