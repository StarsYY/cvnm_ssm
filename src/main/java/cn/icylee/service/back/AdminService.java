package cn.icylee.service.back;

import cn.icylee.bean.Admin;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface AdminService {

    int getAdminTotal(TableParameter tableParameter);

    List<Admin> getPageAdmin(TableParameter tableParameter);

    int saveAdmin(Admin admin);

    Admin getAdminById(int id);

    int updateAdmin(Admin admin);

    int updateStatus(Admin admin);

    int deleteAdmin(int id);

}
