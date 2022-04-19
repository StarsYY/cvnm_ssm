package cn.icylee.service.front;

import cn.icylee.bean.Course;
import cn.icylee.bean.LabelTree;
import cn.icylee.bean.Modular;
import cn.icylee.bean.Rotations;

import java.util.List;

public interface SchoolService {

    List<Rotations> getRotations();

    List<LabelTree> getLeftNav();

    List<Modular> getHotModular();

    List<Course> getHotCourse(int id);

    List<Course> getNewCourse(int id);

    List<Course> getDeveloperStory();

}
