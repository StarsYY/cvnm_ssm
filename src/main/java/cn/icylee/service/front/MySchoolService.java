package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;
import cn.icylee.bean.User;

import java.util.List;

public interface MySchoolService {

    User getMy(String username);

    List<Course> getFavorites(int uid);

    List<Order> getOrder(Order order);

}
