package cn.icylee.service.front;

import cn.icylee.bean.User;

public interface AccountService {

    User getUserByUsername(String username);

    int updateUser(User user);

    int updateCancellation(int uid);

}
