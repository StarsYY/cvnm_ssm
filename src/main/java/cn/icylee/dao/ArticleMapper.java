package cn.icylee.dao;

import cn.icylee.bean.Article;
import cn.icylee.bean.ArticleExample;
import java.util.List;

import cn.icylee.bean.Index;
import cn.icylee.bean.TableParameter;
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

    List<Article> getNewArticle(Index index);

    List<Article> getNewCommentArticle(Index index);

    List<Article> getMostCommentArticle(Index index);

    List<Article> getCollectArticle(Integer uid);
}
