package cn.icylee.controller.back;

import cn.icylee.bean.Order;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.OrderService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/adm/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> xxx(TableParameter tableParameter) {
        tableParameter = orderService.setSelectTool(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", orderService.getOrderTotal(tableParameter));
        map.put("items", orderService.getPageOrder(tableParameter));
        return ResponseData.success(map, "订单列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteOrder(@RequestBody Order order) {
        return orderService.deleteOrder(order.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

}
