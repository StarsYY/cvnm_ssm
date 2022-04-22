package cn.icylee.service.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Label;
import cn.icylee.bean.Plate;
import cn.icylee.bean.TableParameter;

import java.text.ParseException;
import java.util.List;

public interface TagService {

    List<Label> getPageLabel(TableParameter tableParameter) throws ParseException;

    int labelCount(TableParameter tableParameter);

    Label getLabelById(Label label);

    int saveFollowByLabelId(Label label);

    List<Plate> getAllPlate();

    int articleCount(TableParameter tableParameter);

    List<Article> getTagArticle(TableParameter tableParameter);

}
