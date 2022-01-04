package cn.icylee.service.front;

import cn.icylee.bean.User;

public interface LoginFService {

    User getByNickname(String nickname);

    int saveUser(String nickname, String password);

    User getUserByNickname(String nickname);

}
