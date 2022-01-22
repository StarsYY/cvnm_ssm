package cn.icylee.service.back;

import cn.icylee.bean.LabelTree;
import cn.icylee.bean.Plate;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface PlateService {

    int getPlateTotal(TableParameter tableParameter);

    List<Plate> getPagePlate(TableParameter tableParameter);

    List<LabelTree> getOptionPlate(int children);

    Plate savePlate(Plate Plate);

    int updatePlate(Plate Plate);

    int deletePlate(int id);

}
