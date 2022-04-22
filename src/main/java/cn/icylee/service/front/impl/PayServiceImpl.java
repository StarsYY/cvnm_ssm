package cn.icylee.service.front.impl;

import cn.icylee.bean.Course;
import cn.icylee.bean.Order;
import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.OrderMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.GrowService;
import cn.icylee.service.front.PayService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GrowService growService;

    @Override
    public Course getCourseById(int id) {
        return courseMapper.selectByPrimaryKey(id);
    }

    @Override
    public Order saveOrder(Order order) throws ParseException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(order.getUsername());

        order.setUserid(userMapper.selectByExample(userExample).get(0).getUid());
        order.setCreatetime(new Date());
        order.setTransaction(1);
        order.setNumber("SC-" + Tool.getOrderIdByUUId());

        Calendar calendar = Calendar.getInstance();
        String future = String.valueOf(calendar.get(Calendar.YEAR) + 5);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String string = future + time.substring(4);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(string);
        order.setInvalidtime(date);

        return orderMapper.insert(order) > 0 ? order : null;
    }

    @Override
    public int getUserIntegral(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0).getIntegral();
    }

    @Override
    public int saveOrderByIntegralPay(Order order) throws ParseException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(order.getUsername());
        User user = userMapper.selectByExample(userExample).get(0);

        user.setIntegral(user.getIntegral() - order.getPrice());
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {

            order.setUserid(user.getUid());
            order.setCreatetime(new Date());
            order.setPaytime(new Date());
            order.setTransaction(2);
            order.setPayment(2);
            order.setNumber("SC-" + Tool.getOrderIdByUUId());

            Calendar calendar = Calendar.getInstance();
            String future = String.valueOf(calendar.get(Calendar.YEAR) + 5);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String string = future + time.substring(4);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = format.parse(string);
            order.setInvalidtime(date);

            if (orderMapper.insert(order) > 0) {
                return growService.updateDecreaseIntegralFromExchangeCourse(user.getUid(), order.getPrice()) > 0 ? 3 : 2;
            }

            return 0;
        }
        return 0;
    }

    @Override
    public Order getOrderById(int id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        Course course = courseMapper.selectByPrimaryKey(order.getCourseid());
        order.setPrice(course.getPrice());
        order.setName(course.getName());
        return order;
    }

}
