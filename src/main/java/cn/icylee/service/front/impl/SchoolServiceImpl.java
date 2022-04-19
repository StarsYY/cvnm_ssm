package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.ModularMapper;
import cn.icylee.dao.RotationsMapper;
import cn.icylee.dao.VideoMapper;
import cn.icylee.service.back.ModularService;
import cn.icylee.service.front.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    ModularMapper modularMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    ModularService modularService;

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    RotationsMapper rotationsMapper;

    @Override
    public List<Rotations> getRotations() {
        return rotationsMapper.selectByExample(null);
    }

    @Override
    public List<LabelTree> getLeftNav() {
        List<LabelTree> treeList = modularService.getOptionModular(0);
        if (treeList.size() > 8) {
            treeList.subList(8, treeList.size()).clear();
        }
        for (LabelTree labelTree : treeList) {
            StringBuilder ids = new StringBuilder();
            for (LabelTree LT : labelTree.getChildren()) {
                ids.append(LT.getValue()).append(",");
            }
            labelTree.setCourseList(courseMapper.getNavCourseList(ids.toString()));
        }
        return treeList;
    }

    @Override
    public List<Modular> getHotModular() {
        List<Modular> modularList = modularMapper.getHotModular();
        Modular modular = new Modular();
        modular.setId(0);
        modular.setModular("全部");
        modularList.add(0, modular);
        return modularList;
    }

    @Override
    public List<Course> getHotCourse(int id) {
        return courseMapper.getHotCourseList(id);
    }

    @Override
    public List<Course> getNewCourse(int id) {
        return courseMapper.getNewCourseList(id);
    }

    @Override
    public List<Course> getDeveloperStory() {
        ModularExample modularExample = new ModularExample();
        modularExample.createCriteria().andModularEqualTo("开发者故事");
        List<Modular> modularList = modularMapper.selectByExample(modularExample);
        if (modularList.size() != 0) {
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andModularidEqualTo(modularList.get(0).getId());
            courseExample.setOrderByClause(" createtime ASC LIMIT 3");
            List<Course> courseList = courseMapper.selectByExample(courseExample);

            Iterator<Course> iter = courseList.iterator();
            while (iter.hasNext()) {
                Course course = iter.next();
                VideoExample videoExample = new VideoExample();
                videoExample.createCriteria().andCourseidEqualTo(course.getId());
                if (videoMapper.countByExample(videoExample) == 0) {
                    iter.remove();
                }
            }
            return courseList;
        }
        return null;
    }

}
