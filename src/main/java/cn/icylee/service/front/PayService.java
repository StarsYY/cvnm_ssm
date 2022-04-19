package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;

import java.text.ParseException;

public interface PayService {

    Course getCourseById(int id);

    Order saveOrder(Order order) throws ParseException;

    int getUserIntegral(String username);

    int saveOrderByIntegralPay(Order order) throws ParseException;

    Order getOrderById(int id);

}
