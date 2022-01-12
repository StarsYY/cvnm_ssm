package cn.icylee.service.front.impl;

import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.LoginFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoginFServiceImpl implements LoginFService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getByNickname(String nickname) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(nickname);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public int saveUser(String nickname, String password) {
        User user = new User();

        user.setNickname(nickname);

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(user.getNickname());

        if (userMapper.selectByExample(userExample).size() > 0) {
            return 0;
        }

        user.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        user.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        user.setStatus("Enable");
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setPortrait("http://127.0.0.1:8080/upload/image/user/默认头像.png");
        user.setGrow(0);
        user.setIntegral(0);
        return userMapper.insert(user);
    }

    @Override
    public User getUserByNickname(String nickname) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(nickname);
        return userMapper.selectByExample(userExample).get(0);
    }

}
