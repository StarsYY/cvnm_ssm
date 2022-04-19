package cn.icylee.service.front;

import cn.icylee.bean.*;

import java.text.ParseException;
import java.util.List;

public interface IndexService {

    List<LabelTree> getPlateTree();

    List<Article> getArticle(Index index);

    List<Rotation> getRotation();

    List<Article> getHotArticleOnRight();

    List<Label> getHotLabelOnRight();

    List<Article> getRecommendArticleOnRight();

    Plate getPlateById(Plate plate) throws ParseException;

    List<Plate> getPlateChildren(int id);

    int saveFollowByPlateId(Plate plate);

    int saveSign(String username) throws ParseException;

    boolean getSignInStatus(String username) throws ParseException;

}
