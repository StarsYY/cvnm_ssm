package cn.icylee.controller.front;

import cn.icylee.bean.Search;
import cn.icylee.service.front.SearchService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> getSearch(@RequestBody Search search) throws ParseException {
        Map<String, Object> map = new HashMap<>();

        if (search.getChoose() == 0) {
            map.put("total", searchService.getSearchArticleTotal(search) + searchService.getSearchCourseTotal(search));
            map.put("article", searchService.getSearchArticle(search));
            map.put("course", searchService.getSearchCourse(search));
        } else if (search.getChoose() == 1) {
            map.put("total", searchService.getSearchCourseTotal(search));
            map.put("course", searchService.getSearchCourse(search));
        } else if (search.getChoose() == 2) {
            map.put("total", searchService.getSearchArticleTotal(search));
            map.put("article", searchService.getSearchArticle(search));
        }
        return ResponseData.success(map, "搜索内容");
    }

}
