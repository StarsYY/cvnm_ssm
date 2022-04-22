package cn.icylee.service.back.impl;

import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int getUserTotal(TableParameter tableParameter) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (tableParameter.getNickname() != null) {
            criteria.andNicknameLike("%" + tableParameter.getNickname() + "%");
        }
        if (tableParameter.getStatus() != null && !tableParameter.getStatus().equals("")) {
            criteria.andStatusEqualTo(tableParameter.getStatus());
        }
        return userMapper.countByExample(userExample);
    }

    @Override
    public List<User> getPageUser(TableParameter tableParameter) {
        return userMapper.getUserList(tableParameter);
    }

    @Override
    public User saveUser(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(user.getNickname());
        if (userMapper.selectByExample(userExample).size() > 0) {
            user.setUid(-1);
            return user;
        }
        user.setStatus("启用");
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        if (user.getPortrait().equals("") || user.getPortrait() == null) {
            user.setPortrait("http://127.0.0.1:8080/upload/image/user/默认头像.png");
        }
        user.setGrow(0);
        user.setIntegral(0);
        user.setIsdel(0);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());

        if (userMapper.insert(user) > 0) {
            return user;
        } else {
            user.setUid(0);
            return user;
        }
    }

    @Override
    public User getUserByUid(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateUser(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameNotEqualTo(getUserByUid(user.getUid()).getNickname()).andNicknameEqualTo(user.getNickname());
        if (userMapper.selectByExample(userExample).size() > 0) {
            return 0;
        }
        if (!user.getPassword().equals(getUserByUid(user.getUid()).getPassword())) {
            // 对原来的密码进行修改
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        if (user.getStarttime() != null) {
            user.setStatus("禁用");
        }
        user.setUpdatetime(new Date());

        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateStatus(User user) {
        if (user.getStatus().equals(userMapper.selectByPrimaryKey(user.getUid()).getStatus())) {
            if (user.getStatus().equals("启用")) {
                user.setStatus("禁用");
            } else {
                user.setStatus("启用");
            }
            user.setUpdatetime(new Date());

            return userMapper.updateByPrimaryKeySelective(user);
        }
        return 0;
    }

    @Override
    public int deleteUser(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user.getIsdel() == 1) {
            user.setIsdel(0);
        } else {
            user.setIsdel(1);
        }
        user.setUpdatetime(new Date());

        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int deleteUserR(int id) {
        return userMapper.deleteByPrimaryKey(id);
    }

}
