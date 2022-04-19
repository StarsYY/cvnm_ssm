package cn.icylee.service.back;

import cn.icylee.bean.Admin;

public interface LoginService {

    int login(String username, String password);

    Admin getByUsername(String username);

    int updateLogOut(String username);

}
