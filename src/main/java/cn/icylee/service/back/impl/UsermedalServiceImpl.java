package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.MedalMapper;
import cn.icylee.dao.UsermedalMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.UsermedalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsermedalServiceImpl implements UsermedalService {

    @Autowired
    UsermedalMapper usermedalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MedalMapper medalMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (User user : userList) {
                    ids.append(user.getUid()).append(",");
                }
                tableParameter.setIds(ids.toString());
            } else {
                tableParameter.setIds(",0,");
            }
        }
        if (tableParameter.getName() != null && !tableParameter.getName().equals("")) {
            MedalExample medalExample = new MedalExample();
            medalExample.createCriteria().andNameLike("%" + tableParameter.getName() + "%");

            List<Medal> medalList = medalMapper.selectByExample(medalExample);
            if (medalList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (Medal medal : medalList) {
                    ids.append(medal.getId()).append(",");
                }
                tableParameter.setCids(ids.toString());
            } else {
                tableParameter.setCids(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getUsermedalTotal(TableParameter tableParameter) {
        return usermedalMapper.getUsermedalTotal(tableParameter);
    }

    @Override
    public List<Usermedal> getPageUsermedal(TableParameter tableParameter) {
        return usermedalMapper.getUsermedalList(tableParameter);
    }

    @Override
    public int deleteUsermedal(int id) {
        return usermedalMapper.deleteByPrimaryKey(id);
    }
    
}
