package cn.icylee.service.front;

import cn.icylee.bean.Verify;

import java.util.List;

public interface VerifyFService {

    List<Verify> getVerifyByUid(int uid);

    int saveVerify(Verify verify);

}
