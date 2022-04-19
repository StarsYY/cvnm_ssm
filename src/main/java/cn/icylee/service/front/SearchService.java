package cn.icylee.service.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Course;
import cn.icylee.bean.Search;

import java.text.ParseException;
import java.util.List;

public interface SearchService {

    int getSearchArticleTotal(Search search);

    List<Article> getSearchArticle(Search search) throws ParseException;

    int getSearchCourseTotal(Search search);

    List<Course> getSearchCourse(Search search);

}
