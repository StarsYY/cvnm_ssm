package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.CDTService;
import cn.icylee.service.front.GrowService;
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

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GrowService growService;

    @Autowired
    LearningMapper learningMapper;

    @Autowired
    MessageMapper messageMapper;

    public int updateCourse(int id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        course.setWatch(course.getWatch() + 1);
        course.setUpdatetime(new Date());
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    public int getUserByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0).getUid();
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
            int userId = getUserByName(discuss.getAuthor());

            FollowExample followExample = new FollowExample();
            FollowExample.Criteria criteria = followExample.createCriteria();
            criteria.andUseridEqualTo(userId).andDatasourceEqualTo("course").andDataidEqualTo(discuss.getCourseid());
            course.setCollect(followMapper.countByExample(followExample) != 0);
        }

        return course;
    }

    @Override
    public List<Video> getVideoByCourseId(Discuss discuss) {
        Course course = courseMapper.selectByPrimaryKey(discuss.getCourseid());
        if (course.getPrice() > 0) {
            if (!discuss.getAuthor().equals("")) {
                int userId = getUserByName(discuss.getAuthor());
                OrderExample orderExample = new OrderExample();
                orderExample.createCriteria()
                        .andUseridEqualTo(userId)
                        .andCourseidEqualTo(course.getId())
                        .andPaymentIsNotNull()
                        .andPaytimeIsNotNull()
                        .andTransactionEqualTo(2)
                        .andInvalidtimeGreaterThanOrEqualTo(new Date());

                if (orderMapper.countByExample(orderExample) > 0) {
                    VideoExample videoExample = new VideoExample();
                    videoExample.createCriteria().andCourseidEqualTo(discuss.getCourseid());
                    return videoMapper.selectByExample(videoExample);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            VideoExample videoExample = new VideoExample();
            videoExample.createCriteria().andCourseidEqualTo(discuss.getCourseid());
            return videoMapper.selectByExample(videoExample);
        }
    }

    @Override
    public int getIsAdmin(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        if (userMapper.countByExample(userExample) > 0) {
            return userMapper.selectByExample(userExample).get(0).getIsadmin();
        }
        return 0;
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
        int userId = getUserByName(discuss.getAuthor());

        DiscussExample discussExample = new DiscussExample();
        discussExample.createCriteria().andUseridEqualTo(userId).andCourseidEqualTo(discuss.getCourseid()).andDisidEqualTo(0);

        if (discussMapper.countByExample(discussExample) > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Discuss saveDiscuss(Discuss discuss) {
        int userId = getUserByName(discuss.getAuthor());

        DiscussExample discussExample = new DiscussExample();
        if (discuss.getDisid() == 0) {
            discussExample.createCriteria().andUseridEqualTo(userId).andCourseidEqualTo(discuss.getCourseid()).andDisidEqualTo(0);
        } else {
            discussExample.createCriteria().andUseridEqualTo(userId).andCourseidEqualTo(discuss.getCourseid()).andDisidEqualTo(discuss.getDisid());
        }

        if (discussMapper.countByExample(discussExample) == 0) {
            discuss.setUserid(userId);
            if ((discuss.getDisid() > 0)) {
                discuss.setStatus(1);
                discuss.setCreatetime(new Date());

                if (discussMapper.insert(discuss) > 0) {
                    return growService.updateIncreaseIntegralAndGrowFromCommentOrDiscuss(discuss.getUserid()) > 0 ? discuss : null;
                }
            } else {
                discuss.setStatus(0);
            }
            discuss.setCreatetime(new Date());

            if (discussMapper.insert(discuss) > 0) {
                discuss.setId(0);
                return discuss;
            } else {
                return null;
            }
        } else {
            discuss.setId(-1);
            return discuss;
        }
    }

    @Override
    public List<Discuss> getDiscuss(Discuss discuss) {
        List<Discuss> discussList = discussMapper.getDiscuss(discuss);

        for (Discuss dcc: discussList) {
            DiscussExample discussExample = new DiscussExample();
            discussExample.createCriteria().andDisidEqualTo(dcc.getId());
            List<Discuss> discusses = discussMapper.selectByExample(discussExample);
            if (discusses.size() != 0) {
                dcc.setAdmRep(1);
                dcc.setReDis(discusses.get(0));
            } else {
                dcc.setAdmRep(0);
                dcc.setReDis(null);
            }
        }

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
        int userId = getUserByName(discuss.getAuthor());

        FollowExample followExample = new FollowExample();
        FollowExample.Criteria criteriaFollow = followExample.createCriteria();
        criteriaFollow.andUseridEqualTo(userId).andDatasourceEqualTo("course").andDataidEqualTo(discuss.getCourseid());

        List<Follow> followList = followMapper.selectByExample(followExample);
        if (followList.size() == 0) {
            Follow follow = new Follow();
            follow.setUserid(userId);
            follow.setDatasource("course");
            follow.setDataid(discuss.getCourseid());
            follow.setCreatetime(new Date());

            if (followMapper.insert(follow) > 0) {
                return growService.updateIncreaseIntegralAndGrowFromFollow(userId);
            }
        } else {
            if (followMapper.deleteByPrimaryKey(followList.get(0).getId()) > 0) {
                return growService.updateDecreaseIntegralAndGrowFromFollow(userId);
            }
        }

        return 0;
    }

    @Override
    public int deleteDiscuss(int id) {
        int uid = discussMapper.selectByPrimaryKey(id).getUserid();
        if (discussMapper.deleteByPrimaryKey(id) > 0) {
            DiscussExample discussExample = new DiscussExample();
            discussExample.createCriteria().andDisidEqualTo(id);

            if (discussMapper.countByExample(discussExample) > 0) {
                if (discussMapper.deleteByExample(discussExample) > 0) {
                    MessageExample messageExample = new MessageExample();
                    messageExample.createCriteria().andDatasourceEqualTo("discuss").andAdditionEqualTo(id);
                    messageMapper.deleteByExample(messageExample);

                    messageExample.clear();
                    messageExample.createCriteria().andContentEqualTo(String.valueOf(id)).andDatasourceEqualTo("course");
                    messageMapper.deleteByExample(messageExample);

                    return growService.updateDecreaseIntegralAndGrowFromCommentOrDiscuss(uid);
                }
            } else {
                return growService.updateDecreaseIntegralAndGrowFromCommentOrDiscuss(uid);
            }
        }
        return 0;
    }

    @Override
    public int saveLearning(Learning learning) {
        learning.setUserid(getUserByName(learning.getUsername()));

        LearningExample learningExample = new LearningExample();
        learningExample.createCriteria().andUseridEqualTo(learning.getUserid()).andCourseidEqualTo(learning.getCourseid());
        if (learningMapper.countByExample(learningExample) == 0) {
            learning.setCreatetime(new Date());
            return learningMapper.insert(learning);
        }
        return 1;
    }

    @Override
    public List<Course> getRecommendById(int id) {
        return courseMapper.getRecommend(courseMapper.selectByPrimaryKey(id).getModularid());
    }

}
