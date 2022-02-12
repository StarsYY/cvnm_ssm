package cn.icylee.service.back;

import cn.icylee.bean.Verify;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface VerifyService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getVerifyTotal(TableParameter tableParameter);

    List<Verify> getPageVerify(TableParameter tableParameter);

    int updateStatus(Verify verify);

    int deleteVerify(int id);
    
}
