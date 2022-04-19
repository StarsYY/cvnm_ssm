package cn.icylee.service.front;

import cn.icylee.bean.Navigation;
import cn.icylee.bean.User;

import java.util.List;

public interface LoginFService {

    int login(String username, String password);

    User saveUser(String nickname, String password);

    User getUserByNickname(String nickname);

    List<Navigation> getNavigation();

}
