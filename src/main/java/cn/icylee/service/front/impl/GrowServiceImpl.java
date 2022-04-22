package cn.icylee.service.front.impl;

import cn.icylee.bean.Integral;
import cn.icylee.bean.User;
import cn.icylee.dao.IntegralMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.GrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GrowServiceImpl implements GrowService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    IntegralMapper integralMapper;

    @Override
    public int updateIncreaseIntegralAndGrowFromFollow(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        user.setGrow(user.getGrow() + 10);
        user.setIntegral(user.getIntegral() + 10);

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(10);
            integral.setIntegral(10);
            integral.setDescribe("收藏（文章/课程），关注（用户/板块/标签）");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateIncreaseIntegralAndGrowFromPrefer(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        user.setGrow(user.getGrow() + 6);
        user.setIntegral(user.getIntegral() + 6);

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(6);
            integral.setIntegral(6);
            integral.setDescribe("点赞（文章/回复）");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateIncreaseIntegralAndGrowFromArticleOrCourse(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        user.setGrow(user.getGrow() + 50);
        user.setIntegral(user.getIntegral() + 50);

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(50);
            integral.setIntegral(50);
            integral.setDescribe("文章/课程发布成功并通过审核");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateIncreaseIntegralAndGrowFromCommentOrDiscuss(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        user.setGrow(user.getGrow() + 25);
        user.setIntegral(user.getIntegral() + 25);

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(25);
            integral.setIntegral(25);
            integral.setDescribe("评论文章/课程成功并通过审核");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateIncreaseIntegralAndGrowFromSign(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        user.setGrow(user.getGrow() + 6);
        user.setIntegral(user.getIntegral() + 6);

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(6);
            integral.setIntegral(6);
            integral.setDescribe("每日签到");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateDecreaseIntegralAndGrowFromFollow(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        if (user.getGrow() < 10) {
            user.setGrow(0);
        } else {
            user.setGrow(user.getGrow() - 10);
        }
        if (user.getIntegral() < 10) {
            user.setIntegral(0);
        } else {
            user.setIntegral(user.getIntegral() - 10);
        }

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(-10);
            integral.setIntegral(-10);
            integral.setDescribe("取消收藏（文章/课程），取消关注（用户/板块/标签）");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateDecreaseIntegralAndGrowFromPrefer(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        if (user.getGrow() < 6) {
            user.setGrow(0);
        } else {
            user.setGrow(user.getGrow() - 6);
        }
        if (user.getIntegral() < 6) {
            user.setIntegral(0);
        } else {
            user.setIntegral(user.getIntegral() - 6);
        }

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(-6);
            integral.setIntegral(-6);
            integral.setDescribe("取消点赞（文章/回复）");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateDecreaseIntegralAndGrowFromArticleOrCourse(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        if (user.getGrow() < 50) {
            user.setGrow(0);
        } else {
            user.setGrow(user.getGrow() - 50);
        }
        if (user.getIntegral() < 50) {
            user.setIntegral(0);
        } else {
            user.setIntegral(user.getIntegral() - 50);
        }

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(-50);
            integral.setIntegral(-50);
            integral.setDescribe("删除文章/课程");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateDecreaseIntegralAndGrowFromCommentOrDiscuss(int uid) {
        User user = userMapper.selectByPrimaryKey(uid);

        if (user.getGrow() < 25) {
            user.setGrow(0);
        } else {
            user.setGrow(user.getGrow() - 25);
        }
        if (user.getIntegral() < 25) {
            user.setIntegral(0);
        } else {
            user.setIntegral(user.getIntegral() - 25);
        }

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            Integral integral = new Integral();

            integral.setUserid(uid);
            integral.setGrow(-25);
            integral.setIntegral(-25);
            integral.setDescribe("删除文章/课程评论");
            integral.setCreatetime(new Date());

            return integralMapper.insert(integral);
        }
        return 0;
    }

    @Override
    public int updateDecreaseIntegralFromExchangeCourse(int uid, int integral) {
        Integral i = new Integral();

        i.setUserid(uid);
        i.setGrow(-integral);
        i.setIntegral(-integral);
        i.setDescribe("兑换课程");
        i.setCreatetime(new Date());

        return integralMapper.insert(i);
    }

}
