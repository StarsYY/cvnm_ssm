package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.VideoMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public boolean saveVideo(List<Video> videoList, int id) {
        int num = 0;
        for (Video video : videoList) {
            video.setCourseid(id);
            video.setSource("http://127.0.0.1:8080/upload/video/" + video.getFilename());
            video.setCreatetime(new Date());

            num = num + videoMapper.insert(video);
        }
        return num == videoList.size();
    }

    @Override
    public int getVideoByCourseId(int id) {
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andCourseidEqualTo(id);
        return videoMapper.countByExample(videoExample);
    }

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andNameLike("%" + tableParameter.getName() + "%");

            List<Course> courseList = courseMapper.selectByExample(courseExample);
            if (courseList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (Course course : courseList) {
                    ids.append(course.getId()).append(",");
                }
                tableParameter.setIds(ids.toString());
            } else {
                tableParameter.setIds(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getVideoTotal(TableParameter tableParameter) {
        return videoMapper.getVideoTotal(tableParameter);
    }

    @Override
    public List<Video> getPageVideo(TableParameter tableParameter) {
        return videoMapper.getVideoList(tableParameter);
    }

    @Override
    public int deleteVideo(int id) {
        return videoMapper.deleteByPrimaryKey(id);
    }

}
