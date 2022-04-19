package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.SignMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignServiceImpl implements SignService {

    @Autowired
    SignMapper signMapper;

    @Autowired
    UserMapper userMapper;

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
        return tableParameter;
    }

    @Override
    public int getSignTotal(TableParameter tableParameter) {
        return signMapper.getSignTotal(tableParameter);
    }

    @Override
    public List<Sign> getPageSign(TableParameter tableParameter) {
        return signMapper.getSignList(tableParameter);
    }

    @Override
    public int deleteSign(int id) {
        return signMapper.deleteByPrimaryKey(id);
    }
    
}
