package cn.icylee.service.back.impl;

import cn.icylee.bean.TableParameter;
import cn.icylee.bean.User;
import cn.icylee.bean.UserExample;
import cn.icylee.bean.Verify;
import cn.icylee.dao.UserMapper;
import cn.icylee.dao.VerifyMapper;
import cn.icylee.service.back.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Autowired
    VerifyMapper verifyMapper;

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
    public int getVerifyTotal(TableParameter tableParameter) {
        return verifyMapper.getVerifyTotal(tableParameter);
    }

    @Override
    public List<Verify> getPageVerify(TableParameter tableParameter) {
        return verifyMapper.getVerifyList(tableParameter);
    }

    @Override
    public int updateStatus(Verify verify) {
        if (verify.getStatus().equals(verifyMapper.selectByPrimaryKey(verify.getId()).getStatus())) {
            verify.setStatus(1 - verify.getStatus());
            verify.setUpdatetime(new Date());

            return verifyMapper.updateByPrimaryKeySelective(verify);
        }
        return 0;
    }

    @Override
    public int deleteVerify(int id) {
        return verifyMapper.deleteByPrimaryKey(id);
    }

}
