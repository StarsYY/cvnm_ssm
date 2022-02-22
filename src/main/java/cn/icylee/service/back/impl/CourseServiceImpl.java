package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.LabelMapper;
import cn.icylee.dao.ModularMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    ModularMapper modularMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LabelMapper labelMapper;

    @Override
    public int getCourseTotal(TableParameter tableParameter) {
        return courseMapper.getCourseTotal(tableParameter);
    }

    @Override
    public List<Course> getPageCourse(TableParameter tableParameter) {
        List<Course> courseList = courseMapper.getCourseList(tableParameter);
        
        for (Course course : courseList) {
            String author = userMapper.selectByPrimaryKey(course.getUserid()).getNickname();

            String[] labelId = course.getLabelid().substring(1, course.getLabelid().length() - 1).split(",");

            Map<String, String> map = new HashMap<>();
            for (String value : labelId) {
                String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
                map.put(value, label);
            }

            if (course.getId() == 1) {
                course.setScore(4.7);
            }

            course.setModular(modularMapper.selectByPrimaryKey(course.getModularid()).getModular());
            course.setAuthor(author);
            course.setLabelMap(map);
        }

        return courseList;
    }

    @Override
    public Map<String, String> getAllModular() {
        Map<String, String> allModular = new HashMap<>();
        List<Modular> modularList = modularMapper.selectByExample(null);
        for (Modular modular : modularList) {
            allModular.put(String.valueOf(modular.getId()), modular.getModular());
        }
        return allModular;
    }

    @Override
    public int saveCourse(Course course) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andNameEqualTo(course.getName());

        if (courseMapper.selectByExample(courseExample).size() > 0) {
            return 0;
        }

        course.setWatch(0);
        course.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        course.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return courseMapper.insert(course);
    }

    @Override
    public Course getCourseById(int id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        course.setAuthor(userMapper.selectByPrimaryKey(course.getUserid()).getNickname());
        course.setLabelid(course.getLabelid().substring(1, course.getLabelid().length() - 1));
        return course;
    }

    @Override
    public int updateCourse(Course course) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andNameNotEqualTo(getCourseById(course.getId()).getName()).andNameEqualTo(course.getName());
        if (courseMapper.selectByExample(courseExample).size() > 0) {
            return 0;
        }
        course.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    @Override
    public int updateStatus(Course course) {
        if (course.getStatus().equals(getCourseById(course.getId()).getStatus())) {
            if (course.getStatus().equals("已发布")) {
                course.setStatus("待审核");
            } else if (course.getStatus().equals("待审核")) {
                course.setStatus("已发布");
            }
            return courseMapper.updateByPrimaryKeySelective(course);
        }
        return 0;
    }

    @Override
    public int deleteCourse(int id) {
        return courseMapper.deleteByPrimaryKey(id);
    }

}
