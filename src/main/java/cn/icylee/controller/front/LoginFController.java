package cn.icylee.controller.front;

import cn.icylee.bean.User;
import cn.icylee.service.back.UserService;
import cn.icylee.service.front.LoginFService;
import cn.icylee.service.front.SendMessageService;
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
    LoginFService loginFService;

    @Autowired
    SendMessageService sendMessageService;

    @ResponseBody
    @RequestMapping("login")
    public Map<String, Object> Login(String nickname, String password) {
        int num = loginFService.login(nickname, password);

        if (num == -3) {
            return ResponseData.error("您的账户已被封号");
        } else if (num == -2 || num == -1) {
            return ResponseData.error("用户名或密码错误");
        } else if (num == 0) {
            return ResponseData.error("该账号已被禁用");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("token", "user-token");
            map.put("nickname", nickname);
            return ResponseData.success(map, "登陆成功");
        }
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public Map<String, Object> getLoginAdmin(@RequestParam String nickname) {
        User user = loginFService.getUserByNickname(nickname);
        Map<String, Object> info = new HashMap<>();
        user.setGrow(Tool.setLevel(user.getGrow()));
        info.put("loginUser", user);
        return ResponseData.success(info, "登录信息及导航栏");
    }

    @ResponseBody
    @RequestMapping(value = "navigation", method = RequestMethod.GET)
    public Map<String, Object> getNavigation() {
        Map<String, Object> map = new HashMap<>();
        map.put("navigation", loginFService.getNavigation());
        return ResponseData.success(map, "首页顶部导航栏");
    }

    @ResponseBody
    @RequestMapping("register")
    public Map<String, Object> register(String nickname, String password) {
        User user = loginFService.saveUser(nickname, password);

        if (user.getUid() == -1) {
            return ResponseData.error("已有此用户");
        } else if (user.getUid() == 0) {
            return ResponseData.error("网络故障，请重试");
        } else {
            return sendMessageService.saveMessageFromLogin(user) > 0
                    ? ResponseData.success("success", "注册成功")
                    : ResponseData.error("注册成功，消息发送失败");
        }
    }

}
