package cn.icylee.controller.front;

import cn.icylee.bean.Upload;
import cn.icylee.bean.User;
import cn.icylee.service.front.AccountService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> getUserByUsername(@RequestBody String data) {
        String name = JSONObject.fromObject(data).getString("name");
        Map<String, Object> map = new HashMap<>();
        map.put("frontUserUser", accountService.getUserByUsername(name));
        return ResponseData.success(map, "账号中心");
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public Map<String, Object> getAccountInfo(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("frontAccountInfo", accountService.getUserByUsername(user.getNickname()));
        return ResponseData.success(map, "账号");
    }

    @ResponseBody
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public Map<String, Object> getUserInfo(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("frontUserInfo", accountService.getUserByUsername(user.getNickname()));
        return ResponseData.success(map, "userInfo");
    }

    @ResponseBody
    @RequestMapping(value = "user/update", method = RequestMethod.POST)
    public Map<String, Object> updateUser(@RequestBody User user) {
        return accountService.updateUser(user) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("网络拥堵");
    }

    @ResponseBody
    @RequestMapping(value = "user/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "user"), "上传成功");
    }

    @ResponseBody
    @RequestMapping(value = "user/cancellation", method = RequestMethod.POST)
    public Map<String, Object> cancellation(@RequestBody User user) {
        return accountService.updateCancellation(user.getUid()) > 0 ? ResponseData.success("success", "注销成功") : ResponseData.error("网口故障");
    }

}
