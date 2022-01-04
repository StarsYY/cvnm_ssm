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
        return userService.deleteUser(user.getUid()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String serverName = "http://" + request.getServerName() + ":" + request.getServerPort();
        String diskPath = "E:\\IDEA\\ideaWeb";
        String imagePath = "/upload/image/user/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
        String imageName = UUID.randomUUID().toString() + ".png";
        map.put("imagePath", serverName + imagePath + imageName);
        return UploadFile.base64StringToImage(upload.getBase64(), diskPath + imagePath, imageName) ? ResponseData.success(map, "上传成功") : null;
    }

}
