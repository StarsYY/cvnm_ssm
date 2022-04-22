package cn.icylee.controller.front;

import cn.icylee.bean.Order;
import cn.icylee.service.front.PayService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Map;

@Controller
@RequestMapping("/school")
public class PayController {

    @Autowired
    PayService payService;

    @ResponseBody
    @RequestMapping(value = "confirm", method = RequestMethod.GET)
    public Map<String, Object> getCourseById(int id) {
        return ResponseData.success(payService.getCourseById(id), "确认订单");
    }

    @ResponseBody
    @RequestMapping(value = "confirm/pay", method = RequestMethod.POST)
    public Map<String, Object> saveOrder(@RequestBody Order order) throws ParseException {
        order = payService.saveOrder(order);
        return order != null ? ResponseData.success(order.getId(), "添加订单") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "confirm/userIntegral", method = RequestMethod.POST)
    public Map<String, Object> getUserIntegral(@RequestBody Order order) {
        return ResponseData.success(payService.getUserIntegral(order.getUsername()), "获取用户积分");
    }

    @ResponseBody
    @RequestMapping(value = "confirm/pay/integral", method = RequestMethod.POST)
    public Map<String, Object> saveOrderByIntegralPay(@RequestBody Order order) throws ParseException {
        return payService.saveOrderByIntegralPay(order) > 0 ? ResponseData.success("success", "兑换课程") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "order/pay", method = RequestMethod.POST)
    public Map<String, Object> getOrderById(@RequestBody Order order) {
        return ResponseData.success(payService.getOrderById(order.getId()), "订单");
    }

}
