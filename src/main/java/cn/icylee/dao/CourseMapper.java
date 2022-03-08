package cn.icylee.dao;

import cn.icylee.bean.Course;
import cn.icylee.bean.CourseExample;
import java.util.List;

import cn.icylee.bean.Modular;
import cn.icylee.bean.TableParameter;
import org.apache.ibatis.annotations.Param;

public interface CourseMapper {
    int countByExample(CourseExample example);

    int deleteByExample(CourseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Course record);

    int insertSelective(Course record);

    List<Course> selectByExample(CourseExample example);

    Course selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Course record, @Param("example") CourseExample example);

    int updateByExample(@Param("record") Course record, @Param("example") CourseExample example);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKey(Course record);

    int getCourseTotal(TableParameter tableParameter);

    List<Course> getCourseList(TableParameter tableParameter);

    List<Course> getHotCourseList(Integer id);

    List<Course> getNewCourseList(Integer id);

    List<Course> getNavCourseList(String ids);

    List<Course> getAllCourseList(Modular modular);

    int getAllCourseTotal(Modular modular);
}