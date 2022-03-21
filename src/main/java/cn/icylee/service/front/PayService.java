package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;

public interface PayService {

    Course getCourseById(int id);

    int saveOrder(Order order);

}
