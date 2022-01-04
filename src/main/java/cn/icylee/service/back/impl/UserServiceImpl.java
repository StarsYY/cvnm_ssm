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
    public int saveUser(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNicknameEqualTo(user.getNickname());
        if (userMapper.selectByExample(userExample).size() > 0) {
            return 0;
        }
        user.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        user.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        user.setStatus("Enable");
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setGrow("0");
        user.setIntegral("0");
        return userMapper.insert(user);
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
        user.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (!user.getPassword().equals(getUserByUid(user.getUid()).getPassword())) {
            // 对原来的密码进行修改
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateStatus(User user) {
        if (user.getStatus().equals(userMapper.selectByPrimaryKey(user.getUid()).getStatus())) {
            if (user.getStatus().equals("Enable")) {
                user.setStatus("Disable");
            } else {
                user.setStatus("Enable");
            }
            return userMapper.updateByPrimaryKeySelective(user);
        }
        return 0;
    }

    @Override
    public int deleteUser(int id) {
        return userMapper.selectByPrimaryKey(id) != null ? userMapper.deleteByPrimaryKey(id) : 0;
    }

}
