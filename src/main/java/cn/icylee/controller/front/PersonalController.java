package cn.icylee.controller.front;

import cn.icylee.bean.Comment;
import cn.icylee.service.front.DetailService;
import cn.icylee.service.front.PersonalService;
import cn.icylee.utils.ResponseData;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class PersonalController {

    @Autowired
    PersonalService personalService;

    @Autowired
    DetailService detailService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> getUser(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        String loginName = JSONObject.fromObject(data).getString("loginName");
        Map<String, Object> map = new HashMap<>();
        map.put("user", personalService.getUser(name, loginName));
        return ResponseData.success(map, "用户");
    }

    @ResponseBody
    @RequestMapping(value = "overview", method = RequestMethod.POST)
    public Map<String, Object> getOverview(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");

        Map<String ,Object> map = new HashMap<>();

        int numberOfCom = 0;
        for (Integer value : personalService.getCommunication(name).values()) {
            numberOfCom += value;
        }
        map.put("numberOfCom", numberOfCom);

        map.put("communication", personalService.getCommunication(name));
        map.put("newArticle", personalService.getNewArticle(name));
        map.put("follow", personalService.getFollow(name));
        map.put("fans", personalService.getFans(name));
        return ResponseData.success(map, "用户概览");
    }

    @ResponseBody
    @RequestMapping(value = "collect", method = RequestMethod.POST)
    public Map<String, Object> getCollect(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        Map<String, Object> map = new HashMap<>();
        map.put("collect", personalService.getCollect(name));
        return ResponseData.success(map, "我的收藏");
    }

    @ResponseBody
    @RequestMapping(value = "article", method = RequestMethod.POST)
    public Map<String, Object> getArticle(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        String loginName = JSONObject.fromObject(data).getString("loginName");
        Map<String, Object> map = new HashMap<>();
        map.put("article", personalService.getMyArticle(name, loginName));
        return ResponseData.success(map, "用户文章");
    }

    @ResponseBody
    @RequestMapping(value = "fans", method = RequestMethod.POST)
    public Map<String, Object> getMyFans(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        String loginName = JSONObject.fromObject(data).getString("loginName");
        Map<String, Object> map = new HashMap<>();
        map.put("fans", personalService.getMyFans(name, loginName));
        return ResponseData.success(map, "粉丝");
    }

    @ResponseBody
    @RequestMapping(value = "follow", method = RequestMethod.POST)
    public Map<String, Object> getMyFollow(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        String loginName = JSONObject.fromObject(data).getString("loginName");
        Map<String, Object> map = new HashMap<>();
        map.put("follow", personalService.getMyFollow(name, loginName));
        return ResponseData.success(map, "关注");
    }

    @ResponseBody
    @RequestMapping(value = "follow/user", method = RequestMethod.POST)
    public Map<String, Object> followUser(@RequestBody Comment comment) {
        return detailService.saveFollowAuthor(comment) > 0 ? ResponseData.success("success", "关注或取消关注成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

}
