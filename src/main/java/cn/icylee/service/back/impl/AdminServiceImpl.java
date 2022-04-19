package cn.icylee.service.back.impl;

import cn.icylee.bean.Admin;
import cn.icylee.bean.AdminExample;
import cn.icylee.dao.AdminMapper;
import cn.icylee.service.back.AdminService;
import cn.icylee.bean.TableParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public int getAdminTotal(TableParameter tableParameter) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        if (tableParameter.getIntroduction() != null) {
            criteria.andIntroductionLike("%" + tableParameter.getIntroduction() + "%");
        }
        if (tableParameter.getStatus() != null && !tableParameter.getStatus().equals("")) {
            criteria.andStatusEqualTo(Integer.parseInt(tableParameter.getStatus()));
        }
        return adminMapper.countByExample(adminExample);
    }

    @Override
    public List<Admin> getPageAdmin(TableParameter tableParameter) {
        return adminMapper.getAdminList(tableParameter);
    }

    @Override
    public int saveAdmin(Admin admin) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(admin.getUsername());
        if (adminMapper.selectByExample(adminExample).size() > 0) {
            return 0;
        }
        admin.setCreatetime(new Date());
        admin.setUpdatetime(new Date());
        if (admin.getAvatar().equals("")) {
            admin.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }
        admin.setStatus(1);
        admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        return adminMapper.insert(admin);
    }

    @Override
    public Admin getAdminById(int id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateAdmin(Admin admin) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameNotEqualTo(getAdminById(admin.getId()).getUsername()).andUsernameEqualTo(admin.getUsername());
        if (adminMapper.selectByExample(adminExample).size() > 0) {
            return 0;
        }
        admin.setUpdatetime(new Date());
        if (!admin.getPassword().equals(getAdminById(admin.getId()).getPassword())) {
            // 对原来的密码进行修改
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        }
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int updateStatus(Admin admin) {
        if (admin.getStatus().equals(getAdminById(admin.getId()).getStatus())) {
            admin.setStatus(1 - admin.getStatus());
            return adminMapper.updateByPrimaryKeySelective(admin);
        }
        return 0;
    }

    @Override
    public int deleteAdmin(int id) {
        return getAdminById(id) != null ? adminMapper.deleteByPrimaryKey(id) : 0;
    }

}
