package cn.icylee.controller.front;

import cn.icylee.bean.Order;
import cn.icylee.bean.User;
import cn.icylee.service.front.MySchoolService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/school")
public class MySchoolController {

    @Autowired
    MySchoolService mySchoolService;

    @ResponseBody
    @RequestMapping(value = "my", method = RequestMethod.POST)
    public Map<String, Object> getMy(@RequestBody User user) {
        return ResponseData.success(mySchoolService.getMy(user.getNickname()), "我自己");
    }

    @ResponseBody
    @RequestMapping(value = "my/favorites", method = RequestMethod.GET)
    public Map<String, Object> getFavorites(int uid) {
        return ResponseData.success(mySchoolService.getFavorites(uid), "我的收藏");
    }

    @ResponseBody
    @RequestMapping(value = "my/order", method = RequestMethod.POST)
    public Map<String, Object> getOrder(@RequestBody Order order) {
        return ResponseData.success(mySchoolService.getOrder(order), "我的订单");
    }

}
