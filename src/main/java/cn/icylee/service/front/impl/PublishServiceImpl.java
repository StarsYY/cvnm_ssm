package cn.icylee.service.front.impl;

import cn.icylee.bean.Course;
import cn.icylee.bean.CourseExample;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.PublishCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishServiceImpl implements PublishCourseService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper articleMapper;

    @Override
    public List<Course> getCourseDraft(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andUseridEqualTo(uid).andStatusEqualTo("草稿").andIsdelEqualTo(0);
        return articleMapper.selectByExample(courseExample);
    }
}
