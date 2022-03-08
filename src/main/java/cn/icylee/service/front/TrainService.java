package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.Modular;

import java.util.List;

public interface TrainService {

    List<Modular> getBusinessArea();

    List<Modular> getBusinessModule(int ancestor);

    List<Course> getCourseList(Modular modular);

    int getAllCourseTotal(Modular modular);

}
