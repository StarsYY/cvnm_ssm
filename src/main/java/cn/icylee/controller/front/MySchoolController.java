package cn.icylee.controller.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Index;
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
    public Map<String, Object> getFavorites(Index index) {
        return ResponseData.success(mySchoolService.getFavorites(index), "我的收藏");
    }

    @ResponseBody
    @RequestMapping(value = "my/order", method = RequestMethod.POST)
    public Map<String, Object> getOrder(@RequestBody Index index) {
        return ResponseData.success(mySchoolService.getOrder(index), "我的订单");
    }

    @ResponseBody
    @RequestMapping(value = "my/order/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteOrder(@RequestBody Order order) {
        return mySchoolService.deleteOrder(order.getId()) > 0 ? ResponseData.success("success", "删除订单") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "my/class", method = RequestMethod.GET)
    public Map<String, Object> getLearningCourse(Index index) {
        return ResponseData.success(mySchoolService.getLearningCourse(index), "学习中");
    }

    @ResponseBody
    @RequestMapping(value = "my/class/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteLearning(@RequestBody Course course) {
        return mySchoolService.deleteLearning(course.getId()) > 0 ? ResponseData.success("success", "删除学习中") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "my/order/pay", method = RequestMethod.POST)
    public Map<String, Object> updatePayCourse(@RequestBody Order order) {
        order = mySchoolService.updatePayCourse(order.getId());
        return order != null ? ResponseData.success(order, "支付成功") : ResponseData.error("网阔故障");
    }

}
