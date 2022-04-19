package cn.icylee.service.back;

import cn.icylee.bean.Course;
import cn.icylee.bean.TableParameter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CourseService {

    int getCourseTotal(TableParameter tableParameter);

    List<Course> getPageCourse(TableParameter tableParameter);

    Map<String, String> getAllModular();

    int saveCourse(Course course);

    Course getCourseById(int id);

    Map<String, String> searchUser(String nickname);

    int updateCourse(Course course) throws IOException;

    int updateStatus(Course course);

    int deleteCourse(int id);

    int deleteCourseR(int id);

}
