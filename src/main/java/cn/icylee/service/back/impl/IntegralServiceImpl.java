package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.IntegralMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.IntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    IntegralMapper integralMapper;

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
            }
        }
        return tableParameter;
    }

    @Override
    public int getIntegralTotal(TableParameter tableParameter) {
        return integralMapper.getIntegralTotal(tableParameter);
    }

    @Override
    public List<Integral> getPageIntegral(TableParameter tableParameter) {
        return integralMapper.getIntegralList(tableParameter);
    }

    @Override
    public int deleteIntegral(int id) {
        return integralMapper.deleteByPrimaryKey(id);
    }

}
