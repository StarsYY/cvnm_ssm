package cn.icylee.service.back;

import cn.icylee.bean.Sign;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface SignService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getSignTotal(TableParameter tableParameter);

    List<Sign> getPageSign(TableParameter tableParameter);

    int deleteSign(int id);
    
}
