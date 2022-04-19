package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.ArticleMapper;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.OrderMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.DashboardService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public List<Order> getDashboardOrder() {
        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause(" createtime desc limit 0, 8");
        List<Order> orderList = orderMapper.selectByExample(orderExample);

        for (Order order : orderList) {
            order.setPrice(courseMapper.selectByPrimaryKey(order.getCourseid()).getPrice());
        }
        return orderList;
    }

    @Override
    public int getUserCount() {
        return userMapper.countByExample(null);
    }

    @Override
    public int getArticleCount() {
        return articleMapper.countByExample(null);
    }

    @Override
    public String getMoney() {
        List<Order> orderList = orderMapper.getDashMoney();

        double money = 0.00;
        for (Order order : orderList) {
            if (order.getPayment() == 2) {
                money += order.getPrice() * 0.80 / 100;
            } else {
                money += order.getPrice() * 1.00 / 100;
            }
        }
        return String.format("%.2f", money);
    }

    @Override
    public int getCourseCount() {
        return courseMapper.countByExample(null);
    }

    @Override
    public int[] getUserChart() throws ParseException {
        int[] userCount = {0, 0, 0, 0, 0, 0, 0};
        UserExample userExample = new UserExample();
        for (int i = 0; i < userCount.length; i++) {
            userExample.createCriteria().andCreatetimeBetween(Tool.getFewDaysStart(i), Tool.getFewDaysFinal(i));
            userCount[i] = userMapper.countByExample(userExample);
            userExample.clear();
        }
        return userCount;
    }

    @Override
    public int[] getArticleChart() throws ParseException {
        int[] articleCount = {0, 0, 0, 0, 0, 0, 0};
        ArticleExample articleExample = new ArticleExample();
        for (int i = 0; i < articleCount.length; i++) {
            articleExample.createCriteria().andCreatetimeBetween(Tool.getFewDaysStart(i), Tool.getFewDaysFinal(i));
            articleCount[i] = articleMapper.countByExample(articleExample);
            articleExample.clear();
        }
        return articleCount;
    }

    @Override
    public int[] getOrderChart() throws ParseException {
        int[] orderCount = {0, 0, 0, 0, 0, 0, 0};
        OrderExample orderExample = new OrderExample();
        for (int i = 0; i < orderCount.length; i++) {
            orderExample.createCriteria().andCreatetimeBetween(Tool.getFewDaysStart(i), Tool.getFewDaysFinal(i));
            orderCount[i] = orderMapper.countByExample(orderExample);
            orderExample.clear();
        }
        return orderCount;
    }

    @Override
    public int[] getCourseChart() throws ParseException {
        int[] courseCount = {0, 0, 0, 0, 0, 0, 0};
        CourseExample courseExample = new CourseExample();
        for (int i = 0; i < courseCount.length; i++) {
            courseExample.createCriteria().andCreatetimeBetween(Tool.getFewDaysStart(i), Tool.getFewDaysFinal(i));
            courseCount[i] = courseMapper.countByExample(courseExample);
            courseExample.clear();
        }
        return courseCount;
    }

}
