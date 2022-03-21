package cn.icylee.service.front.impl;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;
import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.OrderMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.MySchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MySchoolServiceImpl implements MySchoolService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public User getMy(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        return userList.size() > 0 ? userList.get(0) : null;
    }

    @Override
    public List<Course> getFavorites(int uid) {
        return courseMapper.getFavorites(uid);
    }

    @Override
    public List<Order> getOrder(Order order) {
        if (order.getUserid() == null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(order.getUsername());
            order.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
        }
        return orderMapper.getMyOrder(order);
    }

}
