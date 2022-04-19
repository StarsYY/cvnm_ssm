package cn.icylee.service.back;

import cn.icylee.bean.TableParameter;
import cn.icylee.bean.User;

import java.util.List;

public interface UserService {

    int getUserTotal(TableParameter tableParameter);

    List<User> getPageUser(TableParameter tableParameter);

    User saveUser(User user);

    User getUserByUid(int id);

    int updateUser(User user);

    int updateStatus(User user);

    int deleteUser(int id);

    int deleteUserR(int id);

}
