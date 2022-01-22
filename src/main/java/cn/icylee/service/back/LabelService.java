package cn.icylee.service.back;

import cn.icylee.bean.Label;
import cn.icylee.bean.LabelTree;
import cn.icylee.bean.TableParameter;

import java.util.List;
import java.util.Map;

public interface LabelService {

    List<LabelTree> getCategoryTree();

    int getLabelTotal(TableParameter tableParameter);

    List<Object> getAllRoot();

    Map<String, String> getAllCategory();

    Map<String, Object> getPageLabel(TableParameter tableParameter);

    Label saveLabel(Label label);

    Label getLabelById(int id);

    Label updateLabel(Label label);

    int deleteLabel(int id);

}
