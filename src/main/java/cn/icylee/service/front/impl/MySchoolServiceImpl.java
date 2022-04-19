package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.MySchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MySchoolServiceImpl implements MySchoolService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    VerifyMapper verifyMapper;

    @Autowired
    LearningMapper learningMapper;

    @Override
    public User getMy(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);

        if (userList.size() > 0) {
            VerifyExample verifyExample = new VerifyExample();
            verifyExample.createCriteria().andUseridEqualTo(userList.get(0).getUid()).andPositionEqualTo("讲师").andStatusEqualTo(1);
            if (verifyMapper.countByExample(verifyExample) > 0) {
                userList.get(0).setPosition("讲师");
            }
            return userList.get(0);
        }
        return null;
    }

    @Override
    public List<Course> getFavorites(Index index) {
        return courseMapper.getFavorites(index);
    }

    @Override
    public List<Order> getOrder(Index index) {
        if (index.getUid() == null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(index.getUsername());
            index.setUid(userMapper.selectByExample(userExample).get(0).getUid());
        }
        return orderMapper.getMyOrder(index);
    }

    @Override
    public int deleteOrder(int id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Course> getLearningCourse(Index index) {
        return courseMapper.getLearnCourse(index);
    }

    @Override
    public int deleteLearning(int id) {
        return learningMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Order updatePayCourse(int id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setPaytime(new Date());
        order.setTransaction(2);
        order.setPayment(new Random().nextInt(2));
        return orderMapper.updateByPrimaryKeySelective(order) > 0 ? order : null;
    }

}
