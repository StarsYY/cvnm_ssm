package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.DiscussMapper;
import cn.icylee.dao.MessageMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.DiscussService;
import cn.icylee.service.front.GrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DiscussServiceImpl implements DiscussService {

    @Autowired
    DiscussMapper discussMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GrowService growService;

    @Autowired
    MessageMapper messageMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getCourse() != null && !tableParameter.getCourse().equals("")) {
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andNameLike("%" + tableParameter.getCourse() + "%");

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
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder cids = new StringBuilder();
                for (User user : userList) {
                    cids.append(user.getUid()).append(",");
                }
                tableParameter.setCids(cids.toString());
            } else {
                tableParameter.setCids(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getDiscussTotal(TableParameter tableParameter) {
        return discussMapper.getDiscussTotal(tableParameter);
    }

    @Override
    public List<Discuss> getPageDiscuss(TableParameter tableParameter) {
        List<Discuss> discussList = discussMapper.getDiscussList(tableParameter);
        for (Discuss discuss : discussList) {
            discuss.setUsername(userMapper.selectByPrimaryKey(discuss.getUserid()).getNickname());
            discuss.setCourse(courseMapper.selectByPrimaryKey(discuss.getCourseid()).getName());

            // 社区管理员回复
            DiscussExample discussExample = new DiscussExample();
            discussExample.createCriteria().andDisidEqualTo(discuss.getId());
            List<Discuss> discusses = discussMapper.selectByExample(discussExample);
            if (discusses.size() > 0) {
                discuss.setAdmDis(discusses.get(0).getDiscuss());
            }
        }
        return discussList;
    }

    @Override
    public int updateStatus(Discuss discuss) {
        if (discuss.getStatus().equals(discussMapper.selectByPrimaryKey(discuss.getId()).getStatus())) {
            if (discuss.getStatus().equals(1)) {
                discuss.setStatus(0);
            } else {
                discuss.setStatus(1);

                if (discussMapper.updateByPrimaryKeySelective(discuss) > 0) {
                    return growService.updateIncreaseIntegralAndGrowFromCommentOrDiscuss(discuss.getUserid()) > 0 ? 2 : 0;
                }
            }
            return discussMapper.updateByPrimaryKeySelective(discuss);
        }
        return 0;
    }

    @Override
    public StringBuilder setIds(int id, StringBuilder ids) {
        DiscussExample discussExample = new DiscussExample();
        discussExample.createCriteria().andDisidEqualTo(id);

        List<Discuss> discussList = discussMapper.selectByExample(discussExample);
        for (Discuss discuss : discussList) {
            ids.append(discuss.getId()).append(",");
            setIds(discuss.getId(), ids);
        }
        return ids;
    }

    @Override
    public int[] deleteDiscuss(int id) {
        StringBuilder ids = new StringBuilder();
        ids.append(id).append(",").append(setIds(id, ids));
        String Ids = ids.substring(0, ids.length() / 2 - 1);

        int num = discussMapper.deleteDiscuss(Ids);
        if (num >= 0) {
            MessageExample messageExample = new MessageExample();
            messageExample.createCriteria().andDatasourceEqualTo("discuss").andAdditionEqualTo(id);
            messageMapper.deleteByExample(messageExample);

            messageExample.clear();
            messageExample.createCriteria().andContentEqualTo(String.valueOf(id)).andDatasourceEqualTo("course");
            messageMapper.deleteByExample(messageExample);

            return Arrays.stream(Ids.split(",")).mapToInt(Integer::parseInt).toArray();
        }
        return null;
    }
    
}
