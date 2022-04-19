package cn.icylee.service.back;

import cn.icylee.bean.Rotations;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface RotationsService {

    int getRotationsTotal(TableParameter tableParameter);

    List<Rotations> getPageRotations(TableParameter tableParameter);

    Rotations saveRotations(Rotations rotations);

    int updateRotations(Rotations rotations);

    int deleteRotations(int id);
    
}
