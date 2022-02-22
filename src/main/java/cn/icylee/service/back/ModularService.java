package cn.icylee.service.back;

import cn.icylee.bean.LabelTree;
import cn.icylee.bean.Modular;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface ModularService {

    int getModularTotal(TableParameter tableParameter);

    List<Modular> getPageModular(TableParameter tableParameter);

    List<LabelTree> getOptionModular(int children);

    Modular saveModular(Modular modular);

    int updateModular(Modular modular);

    int deleteModular(int id);
    
}
