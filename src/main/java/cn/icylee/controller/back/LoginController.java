package cn.icylee.controller.back;

import cn.icylee.bean.Admin;
import cn.icylee.service.back.LoginService;
import cn.icylee.service.back.AdminService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/adm")
public class LoginController {

    private static Admin LoginAdmin = null;

    @Autowired
    AdminService adminService;

    @Autowired
    LoginService loginService;

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Map<String, Object> Login(String username, String password) {
        int num = loginService.login(username, password);

        if (num == -2) {
            return ResponseData.error("没有此管理员");
        } else if (num == -1) {
            return ResponseData.error("密码错误");
        } else if (num == 0) {
            return ResponseData.error("该账号已被封禁");
        } else {
            LoginAdmin = loginService.getByUsername(username);
            Map<String, Object> map = new HashMap<>();
            map.put("token", "admin-token");
            return ResponseData.success(map, "登陆成功");
        }
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Map<String, Object> getLoginAdmin() {
        Map<String, Object> info = new HashMap<>();
        String[] arr = {"admin"};
        info.put("introduction", LoginAdmin.getIntroduction());
        info.put("avatar", LoginAdmin.getAvatar());
        info.put("name", LoginAdmin.getUsername());
        info.put("roles", arr);
        return ResponseData.success(info, "登录信息");
    }

    @ResponseBody
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Map<String, Object> LogOut(String name) {
        return loginService.updateLogOut(name) > 0 ? ResponseData.success("退出登陆成功", "退出") : ResponseData.error("未知错误");
    }

}
