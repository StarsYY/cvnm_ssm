package cn.icylee.service.front;

import cn.icylee.bean.*;

import java.util.List;

public interface CDTService {

    int updateCourse(int id);

    Course getCourse(Discuss discuss);

    List<Video> getVideoByCourseId(Discuss discuss);

    int getIsAdmin(String username);

    User getUser(int uid);

    int boolDiscuss(Discuss discuss);

    Discuss saveDiscuss(Discuss discuss);

    List<Discuss> getDiscuss(Discuss discuss);

    int saveFollow(Discuss discuss);

    int deleteDiscuss(int id);

    int saveLearning(Learning learning);

    List<Course> getRecommendById(int id);

}
