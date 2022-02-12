package cn.icylee.service.front.impl;

import cn.icylee.bean.Verify;
import cn.icylee.bean.VerifyExample;
import cn.icylee.dao.VerifyMapper;
import cn.icylee.service.front.VerifyFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class VerifyFServiceImpl implements VerifyFService {

    @Autowired
    VerifyMapper verifyMapper;

    @Override
    public Verify getVerifyByUid(int uid) {
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria().andUseridEqualTo(uid).andTypeEqualTo(1);
        List<Verify> verifyList = verifyMapper.selectByExample(verifyExample);
        return verifyList.size() != 0 ? verifyList.get(0) : null;
    }

    @Override
    public int saveVerify(Verify verify) {
        verify.setStatus(0);
        verify.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        verify.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return verifyMapper.insert(verify);
    }
}
