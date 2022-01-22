package cn.icylee.service.back.impl;

import cn.icylee.bean.Admin;
import cn.icylee.bean.AdminExample;
import cn.icylee.dao.AdminMapper;
import cn.icylee.service.back.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AdminMapper adminMapper;

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
    public int updateOnline(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        Admin admin = adminMapper.selectByExample(adminExample).get(0);
        admin.setOnline(1);
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int updateLogOut(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        Admin admin = adminMapper.selectByExample(adminExample).get(0);
        admin.setOnline(0);
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

}
