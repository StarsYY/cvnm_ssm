package cn.icylee.controller.front;

import cn.icylee.bean.TableParameter;
import cn.icylee.service.front.TagService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagService tagService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showTags(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("labelList", tagService.getPageLabel(tableParameter));
        map.put("count", tagService.labelCount());
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "label", method = RequestMethod.GET)
    public Map<String, Object> getLabelById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("label", tagService.getLabelById(id));
        return ResponseData.success(map, "标签");
    }

    @ResponseBody
    @RequestMapping(value = "plate", method = RequestMethod.GET)
    public Map<String, Object> getAllPlate() {
        Map<String, Object> map = new HashMap<>();
        map.put("plateList", tagService.getAllPlate());
        return ResponseData.success(map, "所有板块");
    }

    @ResponseBody
    @RequestMapping(value = "article", method = RequestMethod.GET)
    public Map<String, Object> getTagArticle(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleList", tagService.getTagArticle(tableParameter));
        map.put("total", tagService.articleCount(tableParameter));
        return ResponseData.success(map, "文章");
    }

}
