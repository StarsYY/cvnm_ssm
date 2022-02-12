package cn.icylee.service.back;

import cn.icylee.bean.Medal;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface MedalService {

    int getMedalTotal(TableParameter tableParameter);

    List<Medal> getPageMedal(TableParameter tableParameter);

    Medal saveMedal(Medal medal);

    int updateMedal(Medal medal);

    int deleteMedal(int id);
    
}
