package cn.icylee.controller.front;

import cn.icylee.bean.User;
import cn.icylee.service.back.UserService;
import cn.icylee.service.front.LoginFService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginFController {

    @Autowired
    UserService userService;

    @Autowired
    LoginFService loginService;

    @ResponseBody
    @RequestMapping("login")
    public Map<String, Object> Login(String nickname, String password) {
        User loginUser = loginService.getByNickname(nickname);
        if (loginUser != null && DigestUtils.md5DigestAsHex(password.getBytes()).equals(loginUser.getPassword())) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", "user-token");
            map.put("nickname", loginUser.getNickname());
            return ResponseData.success(map, "登陆成功");
        }
        return ResponseData.error("用户名或密码错误");
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public Map<String, Object> getLoginAdmin(@RequestParam String nickname) {
        User user = loginService.getUserByNickname(nickname);
        Map<String, Object> info = new HashMap<>();
        user.setGrow(Tool.setLevel(user.getGrow()));
        info.put("loginUser", user);
        return ResponseData.success(info, "登录信息");
    }

    @ResponseBody
    @RequestMapping("register")
    public Map<String, Object> register(String nickname, String password) {
        return loginService.saveUser(nickname, password) > 0 ? ResponseData.success("success", "注册成功") : ResponseData.error("已有此用户");
    }

}
