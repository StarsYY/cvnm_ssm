package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.OrderMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public TableParameter setSelectTool(TableParameter tableParameter) {
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (User user : userList) {
                    ids.append(user.getUid()).append(",");
                }
                tableParameter.setIds(ids.toString());
            } else {
                tableParameter.setIds(",0,");
            }
        }
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andNameLike("%" + tableParameter.getName() + "%");

            List<Course> courseList = courseMapper.selectByExample(courseExample);
            if (courseList.size() > 0) {
                StringBuilder cids = new StringBuilder();
                for (Course course : courseList) {
                    cids.append(course.getId()).append(",");
                }
                tableParameter.setCids(cids.toString());
            } else {
                tableParameter.setCids(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getOrderTotal(TableParameter tableParameter) {
        return orderMapper.getOrderTotal(tableParameter);
    }

    @Override
    public List<Order> getPageOrder(TableParameter tableParameter) {
        return orderMapper.getOrderList(tableParameter);
    }

    @Override
    public int deleteOrder(int id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

}
