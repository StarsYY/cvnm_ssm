package cn.icylee.service.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Index;
import cn.icylee.bean.LabelTree;

import java.util.List;

public interface IndexService {

    List<LabelTree> getPlateTree();

    List<Article> getArticle(Index index);

}
