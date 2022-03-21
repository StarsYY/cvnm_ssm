package cn.icylee.service.front.impl;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.OrderMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.PayService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public Course getCourseById(int id) {
        return courseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveOrder(Order order) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(order.getUsername());
        order.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
        order.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        order.setTransaction(1);
        order.setNumber("SC-" + Tool.getOrderIdByUUId());

        Calendar calendar = Calendar.getInstance();
        String future = String.valueOf(calendar.get(Calendar.YEAR) + 5);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        order.setInvalidtime(future + time.substring(4));
        return orderMapper.insert(order);
    }

}
