package cn.icylee.service.back;

import cn.icylee.bean.Usermedal;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface UsermedalService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getUsermedalTotal(TableParameter tableParameter);

    List<Usermedal> getPageUsermedal(TableParameter tableParameter);

    int deleteUsermedal(int id);
    
}
