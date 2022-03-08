package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Discuss;
import cn.icylee.bean.User;

import java.util.List;

public interface CDTService {

    int updateCourse(int id);

    Course getCourse(Discuss discuss);

    User getUser(int uid);

    int boolDiscuss(Discuss discuss);

    int saveDiscuss(Discuss discuss);

    List<Discuss> getDiscuss(Discuss discuss);

    int saveFollow(Discuss discuss);

}
