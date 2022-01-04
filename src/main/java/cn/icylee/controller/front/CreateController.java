package cn.icylee.controller.front;

import cn.icylee.bean.Article;
import cn.icylee.service.back.ArticleService;
import cn.icylee.service.back.PlateService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/create")
public class CreateController {

    @Autowired
    PlateService plateService;

    @Autowired
    ArticleService articleService;

    @ResponseBody
    @RequestMapping("")
    public Map<String, Object> showCreate() {
        Map<String, Object> map = new HashMap<>();
        map.put("allPlate", plateService.getOptionPlate(0));
        map.put("allLabel", articleService.getLabelTree());
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping("add")
    public Map<String, Object> saveArticle(@RequestBody Article article) {
        return articleService.saveArticle(article) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此篇文章");
    }

}
