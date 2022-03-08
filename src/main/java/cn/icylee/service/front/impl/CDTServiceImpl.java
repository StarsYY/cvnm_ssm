package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.CDTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CDTServiceImpl implements CDTService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DiscussMapper discussMapper;

    @Autowired
    FollowMapper followMapper;

    public int updateCourse(int id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        course.setWatch(course.getWatch() + 1);
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    @Override
    public Course getCourse(Discuss discuss) {
        Course course = courseMapper.selectByPrimaryKey(discuss.getCourseid());

        String[] labelId = course.getLabelid().substring(1, course.getLabelid().length() - 1).split(",");

        Map<String, String> map = new HashMap<>();
        for (String value : labelId) {
            String label = labelMapper.selectByPrimaryKey(Integer.parseInt(value)).getLabel();
            map.put(value, label);
        }
        course.setLabelMap(map);

        DiscussExample discussExample = new DiscussExample();
        discussExample.createCriteria().andCourseidEqualTo(discuss.getCourseid());
        if (discussMapper.countByExample(discussExample) != 0) {
            course.setScore(Double.parseDouble(new DecimalFormat("#.0").format(discussMapper.getAVGCourse(discuss.getCourseid()))));
        }

        if (!discuss.getAuthor().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameEqualTo(discuss.getAuthor());
            int userId = userMapper.selectByExample(userExample).get(0).getUid();

            FollowExample followExample = new FollowExample();
            FollowExample.Criteria criteria = followExample.createCriteria();
            criteria.andUseridEqualTo(userId).andDatasourceEqualTo("course").andDataidEqualTo(discuss.getCourseid());
            course.setCollect(followMapper.countByExample(followExample) != 0);
        }

        return course;
    }

    @Override
    public User getUser(int uid) {
        User user = courseMapper.getUser(uid);
        User u = courseMapper.getUserNum(uid);
        user.setCount(u.getCount());
        user.setWatch(u.getWatch());
        return user;
    }

    @Override
    public int boolDiscuss(Discuss discuss) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(discuss.getAuthor());
        int userid = userMapper.selectByExample(userExample).get(0).getUid();

        DiscussExample discussExample = new DiscussExample();
        discussExample.createCriteria().andUseridEqualTo(userid).andCourseidEqualTo(discuss.getCourseid());

        if (discussMapper.countByExample(discussExample) > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int saveDiscuss(Discuss discuss) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(discuss.getAuthor());
        int userid = userMapper.selectByExample(userExample).get(0).getUid();

        DiscussExample discussExample = new DiscussExample();
        discussExample.createCriteria().andUseridEqualTo(userid).andCourseidEqualTo(discuss.getCourseid());

        if (discussMapper.countByExample(discussExample) == 0) {
            discuss.setUserid(userid);
            discuss.setDisid(0);
            discuss.setStatus(0);
            discuss.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return discussMapper.insert(discuss);
        } else {
            return 2;
        }
    }

    @Override
    public List<Discuss> getDiscuss(Discuss discuss) {
        List<Discuss> discussList = discussMapper.getDiscuss(discuss);

        if (!discuss.getAuthor().equals("")) {
            for (Discuss dc : discussList) {
                if (dc.getAuthor().equals(discuss.getAuthor())) {
                    dc.setRep(1);
                } else {
                    dc.setRep(0);
                }
            }
        }
        return discussList;
    }

    @Override
    public int saveFollow(Discuss discuss) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(discuss.getAuthor());
        int userId = userMapper.selectByExample(userExample).get(0).getUid();

        FollowExample followExample = new FollowExample();
        FollowExample.Criteria criteriaFollow = followExample.createCriteria();
        criteriaFollow.andUseridEqualTo(userId).andDatasourceEqualTo("course").andDataidEqualTo(discuss.getCourseid());

        List<Follow> followList = followMapper.selectByExample(followExample);
        if (followList.size() == 0) {
            Follow follow = new Follow();
            follow.setUserid(userId);
            follow.setDatasource("course");
            follow.setDataid(discuss.getCourseid());
            follow.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return followMapper.insert(follow);
        } else {
            return followMapper.deleteByPrimaryKey(followList.get(0).getId());
        }
    }

}
