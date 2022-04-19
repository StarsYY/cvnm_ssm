package cn.icylee.service.back;

import cn.icylee.bean.Learning;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface LearningService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getLearningTotal(TableParameter tableParameter);

    List<Learning> getPageLearning(TableParameter tableParameter);

    int deleteLearning(int id);
    
}
