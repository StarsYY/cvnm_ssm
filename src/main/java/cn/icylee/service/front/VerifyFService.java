package cn.icylee.service.front;

import cn.icylee.bean.Verify;

public interface VerifyFService {

    Verify getVerifyByUid(int uid);

    int saveVerify(Verify verify);

}
