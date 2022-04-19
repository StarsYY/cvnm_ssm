package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.back.CourseService;
import cn.icylee.service.back.VideoService;
import cn.icylee.service.front.GrowService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    VideoService videoService;

    @Autowired
    GrowService growService;

    @Override
    public int getCourseTotal(TableParameter tableParameter) {
        return courseMapper.getCourseTotal(tableParameter);
    }

    @Override
    public List<Course> getPageCourse(TableParameter tableParameter) {
        List<Course> courseList = courseMapper.getCourseList(tableParameter);
        
        for (Course course : courseList) {
            String[] labelId = course.getLabelid().substring(1, course.getLabelid().length() - 1).split(",");

            Map<String, String> map = new HashMap<>();
            for (String value : labelId) {
                String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
                map.put(value, label);
            }
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
        course.setCreatetime(new Date());
        course.setUpdatetime(new Date());

        courseMapper.insert(course);

        return course.getId();
    }

    @Override
    public Course getCourseById(int id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        course.setAuthor(userMapper.selectByPrimaryKey(course.getUserid()).getNickname());
        course.setLabelid(course.getLabelid().substring(1, course.getLabelid().length() - 1));
        course.setNum(videoService.getVideoByCourseId(course.getId()));
        return course;
    }

    @Override
    public Map<String, String> searchUser(String nickname) {
        List<User> userList = userMapper.getSearchUser(nickname);
        Map<String, String> map = new HashMap<>();
        for (User user : userList) {
            if (user.getStarttime() == null) {
                map.put(user.getUid().toString(), user.getNickname());
            } else if (!(new Date().compareTo(user.getStarttime()) >= 0 && new Date().compareTo(user.getFinaltime()) <= 0)) {
                map.put(user.getUid().toString(), user.getNickname());
            }
        }
        return map;
    }

    @Override
    public int updateCourse(Course course) throws IOException {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andNameNotEqualTo(getCourseById(course.getId()).getName()).andNameEqualTo(course.getName());
        if (courseMapper.selectByExample(courseExample).size() > 0) {
            return 0;
        }

        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andCourseidEqualTo(course.getId());

        if (course.getVideo().length() > 5) {
            if (videoMapper.countByExample(videoExample) > 0) {
                videoMapper.deleteByExample(videoExample);
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Video> videoList = mapper.readValue(course.getVideo(), new TypeReference<List<Video>>() {});
            videoService.saveVideo(videoList, course.getId());
        }

        course.setUpdatetime(new Date());

        return courseMapper.updateByPrimaryKeySelective(course);
    }

    @Override
    public int updateStatus(Course course) {
        if (course.getStatus().equals(getCourseById(course.getId()).getStatus())) {
            if (course.getStatus().equals("已发布")) {
                course.setStatus("待审核");
            } else if (course.getStatus().equals("待审核")) {
                course.setStatus("已发布");
                course.setUpdatetime(new Date());

                if (courseMapper.updateByPrimaryKeySelective(course) > 0) {
                    return growService.updateIncreaseIntegralAndGrowFromArticleOrCourse(course.getUserid());
                }
            }
            course.setUpdatetime(new Date());

            return courseMapper.updateByPrimaryKeySelective(course);
        }
        return 0;
    }

    @Override
    public int deleteCourse(int id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        if (course.getIsdel() == 1) {
            course.setIsdel(0);
        } else {
            course.setIsdel(1);
        }
        course.setUpdatetime(new Date());
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    @Override
    public int deleteCourseR(int id) {
        return courseMapper.deleteByPrimaryKey(id);
    }

}
