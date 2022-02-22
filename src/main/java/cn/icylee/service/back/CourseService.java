package cn.icylee.service.back;

import cn.icylee.bean.Course;
import cn.icylee.bean.TableParameter;

import java.util.List;
import java.util.Map;

public interface CourseService {

    int getCourseTotal(TableParameter tableParameter);

    List<Course> getPageCourse(TableParameter tableParameter);

    Map<String, String> getAllModular();

    int saveCourse(Course course);

    Course getCourseById(int id);

    int updateCourse(Course course);

    int updateStatus(Course course);

    int deleteCourse(int id);

}
