package cn.icylee.controller.front;

import cn.icylee.bean.Label;
import cn.icylee.bean.Plate;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.front.IndexService;
import cn.icylee.service.front.TagService;
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
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    IndexService indexService;

    @ResponseBody
    @RequestMapping(value = "right", method = RequestMethod.GET)
    public Map<String, Object> getLabelRight() {
        Map<String, Object> map = new HashMap<>();
        map.put("labelRight", indexService.getHotLabelOnRight());
        return ResponseData.success(map, "右侧标签");
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showTags(TableParameter tableParameter) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("labelList", tagService.getPageLabel(tableParameter));
        map.put("count", tagService.labelCount());
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "label", method = RequestMethod.GET)
    public Map<String, Object> getLabelById(Label label) {
        Map<String, Object> map = new HashMap<>();
        map.put("label", tagService.getLabelById(label));
        return ResponseData.success(map, "标签");
    }

    @ResponseBody
    @RequestMapping(value = "label/follow", method = RequestMethod.POST)
    public Map<String, Object> followLabel(@RequestBody Label label) {
        int num = tagService.saveFollowByLabelId(label);

        if (num == -2) {
            return ResponseData.error("不存在此用户，请联系管理员");
        } else {
            return num > 0 ? ResponseData.success("success", "关注或取消关注板块成功") : ResponseData.error("网络故障，请稍后再试");
        }
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
