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
    public List<Verify> getVerifyByUid(int uid) {
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria().andUseridEqualTo(uid);
        return verifyMapper.selectByExample(verifyExample);
    }

    @Override
    public int saveVerify(Verify verify) {
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria().andUseridEqualTo(verify.getUserid()).andPositionEqualTo(verify.getPosition());
        int num = verifyMapper.countByExample(verifyExample);

        if (num == 0) {
            verify.setId(null);
            verify.setStatus(0);
            verify.setCreatetime(new Date());
            verify.setUpdatetime(new Date());
            return verifyMapper.insert(verify);
        } else {
            verify.setId(verifyMapper.selectByExample(verifyExample).get(0).getId());
            verify.setStatus(0);
            verify.setUpdatetime(new Date());
            return verifyMapper.updateByPrimaryKeySelective(verify);
        }
    }
}
