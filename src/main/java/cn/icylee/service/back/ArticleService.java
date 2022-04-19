package cn.icylee.service.back;

import cn.icylee.bean.*;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    List<LabelTree> getLabelTree();

    List<Object> getAllRoot();

    List<Object> getAllCategory();

    Map<String, String> getAllLabel();

    Map<String, String> getAllPlate();

    StringBuilder getAllPid(int pid);

    TableParameter setIdsTool(TableParameter tableParameter);

    int getArticleTotal(TableParameter tableParameter);

    Map<String, Object> getPageArticle(TableParameter tableParameter);

    Map<String, String> searchUser(String nickname);

    int saveArticle(Article article);

    Article getArticleById(int id);

    int updateArticle(Article article);

    int updateStatus(Article article);

    int updateTag(Article article);

    int updateRTag(Article article);

    int updateHot(Article article);

    int updateRHot(Article article);

    int deleteArticle(int id);

    int deleteArticleR(int id);

    int saveMessageByArticle(Message message);

}
