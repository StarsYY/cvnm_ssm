package cn.icylee.service.back;

import cn.icylee.bean.Admin;

public interface LoginService {

    Admin getByUsername(String username);

    int updateOnline(String username);

    int updateLogOut(String username);

}
