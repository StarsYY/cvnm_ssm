package cn.icylee.controller.front;

import cn.icylee.bean.Index;
import cn.icylee.service.front.IndexService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    IndexService indexService;

    @ResponseBody
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Map<String, Object> index() {
        Map<String, Object> map = new HashMap<>();
        map.put("labelTree", indexService.getPlateTree());
        return ResponseData.success(map, "首页");
    }

    @ResponseBody
    @RequestMapping(value = "contentTags", method = RequestMethod.GET)
    public Map<String, Object> contentTags(Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("contentTags", Tool.tags(index.getDefOrTree()));
        return ResponseData.success(map, "contentTags");
    }

    @ResponseBody
    @RequestMapping(value = "article", method = RequestMethod.GET)
    public Map<String, Object> getArticle(Index index) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", indexService.getArticle(index));
        map.put("length", indexService.getArticle(index).size());
        return ResponseData.success(map, "文章");
    }

}
