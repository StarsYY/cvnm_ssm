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
        Admin loginAdmin = loginService.getByUsername(username);
        if (loginAdmin != null && DigestUtils.md5DigestAsHex(password.getBytes()).equals(loginAdmin.getPassword())) {
            LoginAdmin = loginAdmin;
            Map<String, Object> map = new HashMap<>();
            map.put("token", "admin-token");
            return loginService.updateOnline(username) > 0 ? ResponseData.success(map, "登陆成功") : ResponseData.error("未知错误");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Map<String, Object> getLoginAdmin() {
        Map<String, Object> info = new HashMap<>();
        String[] arr = {LoginAdmin.getUsername()};
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
