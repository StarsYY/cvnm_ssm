package cn.icylee.service.front;

import cn.icylee.bean.Course;

import java.util.List;

public interface PublishCourseService {

    List<Course> getCourseDraft(String username);

}
