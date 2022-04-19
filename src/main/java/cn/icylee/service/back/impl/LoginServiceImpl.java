package cn.icylee.service.back.impl;

import cn.icylee.bean.Admin;
import cn.icylee.bean.AdminExample;
import cn.icylee.dao.AdminMapper;
import cn.icylee.service.back.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public int login(String username, String password) {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username);
        if (adminMapper.countByExample(adminExample) > 0) {
            Admin admin = adminMapper.selectByExample(adminExample).get(0);

            if (admin.getStatus() == 0) {
                return 0;
            } else {
                if (admin.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return -2;
    }

    @Override
    public Admin getByUsername(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public int updateLogOut(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        Admin admin = adminMapper.selectByExample(adminExample).get(0);
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

}
