package cn.icylee.service.front;

import cn.icylee.bean.*;

import java.util.List;

public interface MySchoolService {

    User getMy(String username);

    List<Course> getFavorites(Index index);

    List<Order> getOrder(Index index);

    int deleteOrder(int id);

    List<Course> getLearningCourse(Index index);

    int deleteLearning(int id);

    Order updatePayCourse(int id);

    List<Course> getMyCourse(Index index);

    int deleteMyCourse(int id);

}
