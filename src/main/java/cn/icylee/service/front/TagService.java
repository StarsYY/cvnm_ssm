package cn.icylee.service.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Label;
import cn.icylee.bean.Plate;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface TagService {

    List<Label> getPageLabel(TableParameter tableParameter);

    int labelCount();

    Label getLabelById(int id);

    List<Plate> getAllPlate();

    int articleCount(TableParameter tableParameter);

    List<Article> getTagArticle(TableParameter tableParameter);

}
