package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Discuss;

import java.util.List;

public interface CDTService {

    Course getCourse(Discuss discuss);

    int boolDiscuss(Discuss discuss);

    int saveDiscuss(Discuss discuss);

    List<Discuss> getDiscuss(Discuss discuss);

    int saveFollow(Discuss discuss);

}
