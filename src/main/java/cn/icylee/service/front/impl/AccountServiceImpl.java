package cn.icylee.service.front.impl;

import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0);
    }

    @Override
    public int updateUser(User user) {
        user.setUpdatetime(new Date());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateCancellation(int uid) {
        // article comment course discuss follow integral learning message
        // order prefer report sign usermedal verify video user
        // 单关联：integral sign usermedal verify
        User user = userMapper.selectByPrimaryKey(uid);
        user.setStatus("禁用");
        user.setNickname("用户已注销_" + new Date().getTime());
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
