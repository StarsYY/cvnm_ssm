package cn.icylee.dao;

import cn.icylee.bean.*;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArticleMapper {
    int countByExample(ArticleExample example);

    int deleteByExample(ArticleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    List<Article> selectByExample(ArticleExample example);

    Article selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByExample(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    int getArticleTotal(TableParameter tableParameter);

    List<Article> getArticleList(TableParameter tableParameter);

    List<Article> getMostHotArticle(Index index);

    List<Article> getIndexArticle(Index index);

    List<Article> getFollowUser(Index index);

    List<Article> getFollowPlate(Index index);

    List<Article> getFollowLabel(Index index);

    List<Article> getCollectArticle(Index index);

    List<Article> getTagArticle(TableParameter tableParameter);

    int getCountTagArticle(TableParameter tableParameter);

    List<Article> getMyDraft(Index index);

    List<Article> getMyAudit(Index index);

    int getWatch(Integer uid);

    List<Article> getSearchArticle(Search search);

    int getSearchTotal(Search search);
}
