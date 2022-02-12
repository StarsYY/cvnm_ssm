package cn.icylee.service.back.impl;

import cn.icylee.bean.Medal;
import cn.icylee.bean.MedalExample;
import cn.icylee.bean.TableParameter;
import cn.icylee.dao.MedalMapper;
import cn.icylee.service.back.MedalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MedalServiceImpl implements MedalService {

    @Autowired
    MedalMapper medalMapper;

    @Override
    public int getMedalTotal(TableParameter tableParameter) {
        MedalExample medalExample = new MedalExample();
        MedalExample.Criteria criteria = medalExample.createCriteria();
        if (tableParameter.getTitle() != null && !tableParameter.getTitle().equals("")) {
            criteria.andNameLike("%" + tableParameter.getTitle() + "%");
        }
        if (tableParameter.getIntroduction() != null && !tableParameter.getIntroduction().equals("")) {
            criteria.andDescribeLike("%" + tableParameter.getIntroduction() + "%");
        }
        return medalMapper.countByExample(medalExample);
    }

    @Override
    public List<Medal> getPageMedal(TableParameter tableParameter) {
        return medalMapper.getMedalList(tableParameter);
    }

    @Override
    public Medal saveMedal(Medal medal) {
        MedalExample medalExample = new MedalExample();
        medalExample.createCriteria().andNameEqualTo(medal.getName());
        if (medalMapper.selectByExample(medalExample).size() > 0) {
            return null;
        }
        medal.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        medal.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        medalMapper.insert(medal);
        return medal;
    }

    @Override
    public int updateMedal(Medal medal) {
        MedalExample medalExample = new MedalExample();
        MedalExample.Criteria criteria = medalExample.createCriteria();
        criteria.andNameNotEqualTo(medalMapper.selectByPrimaryKey(medal.getId()).getName()).andNameEqualTo(medal.getName());
        if (medalMapper.selectByExample(medalExample).size() > 0) {
            return 0;
        }
        medal.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return medalMapper.updateByPrimaryKeySelective(medal);
    }

    @Override
    public int deleteMedal(int id) {
        return medalMapper.deleteByPrimaryKey(id);
    }
}
