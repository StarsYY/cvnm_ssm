package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.MessageMapper;
import cn.icylee.dao.NavigationMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.dao.VerifyMapper;
import cn.icylee.service.front.LoginFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class LoginFServiceImpl implements LoginFService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    VerifyMapper verifyMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    NavigationMapper navigationMapper;

    @Override
    public int login(String username, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username).andIsdelEqualTo(0);
        if (userMapper.countByExample(userExample) > 0) {
            User user = userMapper.selectByExample(userExample).get(0);

            if (user.getStatus().equals("禁用")) {
                return 0;
            } else if (user.getStarttime() != null) {
                if (new Date().compareTo(user.getStarttime()) >= 0 && new Date().compareTo(user.getFinaltime()) <= 0) {
                    return -3;
                }
            } else {
                if (user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return -2;
    }

    @Override
    public User saveUser(String nickname, String password) {
        User user = new User();

        user.setNickname(nickname);

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(user.getNickname());

        if (userMapper.selectByExample(userExample).size() > 0) {
            user.setUid(-1);
            return user;
        }

        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        user.setStatus("启用");
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setPortrait("http://127.0.0.1:8080/upload/image/user/默认头像.png");
        user.setGrow(0);
        user.setIntegral(0);
        user.setIsadmin(0);
        user.setIsdel(0);

        if (userMapper.insert(user) > 0) {
            return user;
        } else {
            user.setUid(0);
            return user;
        }
    }

    @Override
    public User getUserByNickname(String nickname) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(nickname);
        List<User> userList = userMapper.selectByExample(userExample);

        if (userList.size() > 0) {
            VerifyExample verifyExample = new VerifyExample();
            verifyExample.createCriteria().andUseridEqualTo(userList.get(0).getUid()).andPositionEqualTo("讲师").andStatusEqualTo(1);
            if (verifyMapper.countByExample(verifyExample) > 0) {
                userList.get(0).setPosition("讲师");
            }

            MessageExample messageExample = new MessageExample();
            messageExample.createCriteria().andReceiveuidEqualTo(userList.get(0).getUid()).andReadEqualTo(0);
            userList.get(0).setUnreadMessage(messageMapper.countByExample(messageExample));

            return userList.get(0);
        }
        return null;
    }

    @Override
    public List<Navigation> getNavigation() {
        return navigationMapper.selectByExample(null);
    }

}
