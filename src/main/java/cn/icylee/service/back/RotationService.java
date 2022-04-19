package cn.icylee.service.back;

import cn.icylee.bean.Rotation;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface RotationService {

    int getRotationTotal(TableParameter tableParameter);

    List<Rotation> getPageRotation(TableParameter tableParameter);

    Rotation saveRotation(Rotation rotation);

    int updateRotation(Rotation rotation);

    int deleteRotation(int id);
    
}
