package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.SearchService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    ModularMapper modularMapper;

    @Override
    public int getSearchArticleTotal(Search search) {
        return articleMapper.getSearchTotal(search);
    }

    @Override
    public List<Article> getSearchArticle(Search search) throws ParseException {
        if (search.getTime() > 0) {
            search.setStartTime(Tool.time(search.getTime()));
        }

        List<Article> articleList = articleMapper.getSearchArticle(search);

        for (Article article : articleList) {
            article.setUrl("http://localhost:8088/detail/" + article.getId());

            if (search.getUsername() != null && !search.getUsername().equals("")) {
                UserExample userExample = new UserExample();
                userExample.createCriteria().andNicknameEqualTo(search.getUsername());
                int uid = userMapper.selectByExample(userExample).get(0).getUid();

                PreferExample preferExample = new PreferExample();
                preferExample.createCriteria().andPushEqualTo(1).andUseridEqualTo(uid).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

                article.setLike(preferMapper.countByExample(preferExample) > 0);

                PreferExample preferExample2 = new PreferExample();
                preferExample2.createCriteria().andPushEqualTo(0).andUseridEqualTo(uid).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

                article.setUnlike(preferMapper.countByExample(preferExample2) > 0);
            }
        }
        return articleList;
    }

    @Override
    public int getSearchCourseTotal(Search search) {
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andNameLike("%" + search.getSearch() + "%").andStatusEqualTo("已发布");
        return courseMapper.countByExample(courseExample);
    }

    @Override
    public List<Course> getSearchCourse(Search search) {
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andNameLike("%" + search.getSearch() + "%").andStatusEqualTo("已发布");
        courseExample.setOrderByClause(" createtime desc limit " + search.getPage() + ", " + search.getLimit());
        List<Course> courseList = courseMapper.selectByExample(courseExample);

        for (Course course : courseList) {
            course.setUrl("http://localhost:8088/school/purchase/" + course.getId());

            Modular modular = modularMapper.selectByPrimaryKey(course.getModularid());

            List<Modular> modularName = new ArrayList<>();
            modularName.add(0, modular);
            if (modular.getAncestor() > 0) {
                modularName.add(0, modularMapper.selectByPrimaryKey(modular.getAncestor()));
            }

            course.setModularName(modularName);
        }
        return courseList;
    }

}
