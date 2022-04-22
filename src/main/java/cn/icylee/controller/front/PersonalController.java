package cn.icylee.controller.front;

import cn.icylee.bean.*;
import cn.icylee.service.front.DetailService;
import cn.icylee.service.front.PersonalService;
import cn.icylee.service.front.UserMedalService;
import cn.icylee.utils.ResponseData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class PersonalController {

    @Autowired
    PersonalService personalService;

    @Autowired
    DetailService detailService;

    @Autowired
    UserMedalService userMedalService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> getUser(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        String loginName = JSONObject.fromObject(data).getString("loginName");
        Map<String, Object> map = new HashMap<>();
        map.put("user", personalService.getUser(name, loginName));
        if (userMedalService.saveUserMedal(name) > 0) {
            map.put("userMedal", personalService.getUserMedal(name));
        }
        map.put("allMedal", personalService.getAllMedal());
        return ResponseData.success(map, "用户");
    }

    @ResponseBody
    @RequestMapping(value = "editSummary", method = RequestMethod.POST)
    public Map<String, Object> updateUserSummary(@RequestBody User user) {
        return personalService.updateUserSummary(user) > 0 ? ResponseData.success("success", "修改简介成功") : ResponseData.error("网络故障");
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
    @RequestMapping(value = "expert", method = RequestMethod.POST)
    public Map<String, Object> getExpert(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");

        Map<String, Object> map = new HashMap<>();
        map.put("expert", personalService.getExpert(name));
        return ResponseData.success(map, "推荐专家");
    }

    @ResponseBody
    @RequestMapping(value = "collect", method = RequestMethod.POST)
    public Map<String, Object> getCollect(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("collect", personalService.getCollect(index));
        return ResponseData.success(map, "我的收藏");
    }

    @ResponseBody
    @RequestMapping(value = "message/system", method = RequestMethod.POST)
    public Map<String, Object> getMySystemMessage(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("systemMessage", personalService.getMySystemMessage(index));
        return ResponseData.success(map, "系统消息");
    }

    @ResponseBody
    @RequestMapping(value = "message/administrator", method = RequestMethod.POST)
    public Map<String, Object> getMyAdministratorMessage(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("administratorMessage", personalService.getMyAdministratorMessage(index));
        return ResponseData.success(map, "管理员消息");
    }

    @ResponseBody
    @RequestMapping(value = "message/read", method = RequestMethod.POST)
    public Map<String, Object> updateMessage(@RequestBody Message message) {
        return personalService.updateMessage(message) > 0 ? ResponseData.success("success", "设为已读") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "message/readAll", method = RequestMethod.POST)
    public Map<String, Object> updateMessageAll(@RequestBody Message message) {
        return personalService.updateMessageAll(message) > 0 ? ResponseData.success("success", "全部设为已读") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "message/deleteAll", method = RequestMethod.POST)
    public Map<String, Object> deleteSelectMessage(@RequestBody String data) throws IOException {
        String idsString = JSONObject.fromObject(data).getString("idsJSON");
        ObjectMapper mapper = new ObjectMapper();
        int[] ids = mapper.readValue(idsString, new TypeReference<int[]>(){});
        return personalService.deleteSelectMessage(ids) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "article", method = RequestMethod.POST)
    public Map<String, Object> getArticle(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", personalService.getMyArticle(index));
        return ResponseData.success(map, "用户文章");
    }

    @ResponseBody
    @RequestMapping(value = "article/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMyArticle(@RequestBody String data) {
        int id = Integer.parseInt(JSONObject.fromObject(data).getString("id"));
        return personalService.deleteMyArticle(id) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "fans", method = RequestMethod.POST)
    public Map<String, Object> getMyFans(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("fans", personalService.getMyFans(index));
        return ResponseData.success(map, "粉丝");
    }

    @ResponseBody
    @RequestMapping(value = "follow", method = RequestMethod.POST)
    public Map<String, Object> getMyFollow(@RequestBody Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("follow", personalService.getMyFollow(index));
        return ResponseData.success(map, "关注");
    }

    @ResponseBody
    @RequestMapping(value = "follow/plate", method = RequestMethod.POST)
    public Map<String, Object> getMyFollowPlate(@RequestBody Index index) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("followPlate", personalService.getMyFollowPlate(index));
        return ResponseData.success(map, "关注的板块");
    }

    @ResponseBody
    @RequestMapping(value = "follow/label", method = RequestMethod.POST)
    public Map<String, Object> getMyFollowLabel(@RequestBody Index index) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("followLabel", personalService.getMyFollowLabel(index));
        return ResponseData.success(map, "关注的标签");
    }

    @ResponseBody
    @RequestMapping(value = "follow/user", method = RequestMethod.POST)
    public Map<String, Object> followUser(@RequestBody Comment comment) {
        return detailService.saveFollowAuthor(comment) > 0 ? ResponseData.success("success", "关注或取消关注成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "verify", method = RequestMethod.POST)
    public Map<String, Object> getVerify(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        return ResponseData.success(personalService.getVerify(name), "认证");
    }

    @ResponseBody
    @RequestMapping(value = "integral", method = RequestMethod.POST)
    public Map<String, Object> getIntegral(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        Integer page = Integer.parseInt(JSONObject.fromObject(data).getString("page"));
        Map<String, Object> map = new HashMap<>();
        map.put("integral", personalService.getIntegral(name, page));
        map.put("total", personalService.getIntegralTotal(name));
        return ResponseData.success(map, "积分");
    }

    @ResponseBody
    @RequestMapping(value = "draft", method = RequestMethod.POST)
    public Map<String, Object> getMyDraft(@RequestBody Index index) {
        return ResponseData.success(personalService.getMyDraft(index), "草稿");
    }

    @ResponseBody
    @RequestMapping(value = "draft/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMyDraft(@RequestBody String data) {
        int id = Integer.parseInt(JSONObject.fromObject(data).getString("id"));
        return personalService.deleteMyDraft(id) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    public Map<String, Object> getMyAudit(@RequestBody Index index) {
        return ResponseData.success(personalService.getMyAudit(index), "待审核");
    }

    @ResponseBody
    @RequestMapping(value = "audit/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMyAudit(@RequestBody String data) {
        int id = Integer.parseInt(JSONObject.fromObject(data).getString("id"));
        return personalService.deleteMyAudit(id) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "reply", method = RequestMethod.POST)
    public Map<String, Object> getMyComment(@RequestBody Index index) {
        return ResponseData.success(personalService.getMyComment(index), "我的回复");
    }

    @ResponseBody
    @RequestMapping(value = "reply/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMyComment(@RequestBody String data) {
        int id = Integer.parseInt(JSONObject.fromObject(data).getString("id"));
        int[] ids = personalService.deleteMyComment(id);
        return ids.length > 0 ? ResponseData.success(ids, "删除成功") : ResponseData.error("网络故障");
    }

}
