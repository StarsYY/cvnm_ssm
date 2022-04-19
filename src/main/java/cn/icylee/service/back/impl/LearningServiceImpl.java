package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.LearningMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningServiceImpl implements LearningService {

    @Autowired
    LearningMapper learningMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
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
                StringBuilder ids = new StringBuilder();
                for (Course course : courseList) {
                    ids.append(course.getId()).append(",");
                }
                tableParameter.setCids(ids.toString());
            } else {
                tableParameter.setCids(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getLearningTotal(TableParameter tableParameter) {
        return learningMapper.getLearningTotal(tableParameter);
    }

    @Override
    public List<Learning> getPageLearning(TableParameter tableParameter) {
        return learningMapper.getLearningList(tableParameter);
    }

    @Override
    public int deleteLearning(int id) {
        return learningMapper.deleteByPrimaryKey(id);
    }
    
}
