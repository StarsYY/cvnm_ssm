package cn.icylee.controller.back;

import cn.icylee.bean.User;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.UserService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/adm/user")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllUser(TableParameter tableParameter) {
        int total = userService.getUserTotal(tableParameter);
        List<User> userList = userService.getPageUser(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", userList);
        return ResponseData.success(map, "消费者列表");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getUserById(int id) {
        return ResponseData.success(userService.getUserByUid(id), "消费者详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveUser(@RequestBody User user) {
        return userService.saveUser(user) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此用户");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateUser(@RequestBody User user) {
        return userService.updateUser(user) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此用户");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody User user) {
        return userService.updateStatus(user) > -1 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteUser(@RequestBody User user) {
        return userService.deleteUser(user.getUid()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "deleteR", method = RequestMethod.POST)
    public Map<String, Object> deleteUserR(@RequestBody User user) {
        return userService.deleteUserR(user.getUid()) > 0 ? ResponseData.success("success", "彻底删除") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "user"), "上传");
    }

}
