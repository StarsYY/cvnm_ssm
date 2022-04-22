package cn.icylee.controller.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Index;
import cn.icylee.bean.Plate;
import cn.icylee.bean.Sign;
import cn.icylee.service.front.IndexService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
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
        map.put("contentTags", Tool.tags(index.getDefOrTree(), index.getLeftId()));
        return ResponseData.success(map, "contentTags");
    }

    @ResponseBody
    @RequestMapping(value = "article", method = RequestMethod.GET)
    public Map<String, Object> getArticle(Index index) {
        List<Article> articleList = indexService.getArticle(index);
        Map<String, Object> map = new HashMap<>();
        map.put("article", articleList);
        if (articleList != null) {
            map.put("length", articleList.size());
        } else {
            map.put("length", 0);
        }
        return ResponseData.success(map, "文章");
    }

    @ResponseBody
    @RequestMapping(value = "index/right", method = RequestMethod.GET)
    public Map<String, Object> getIndexRight() {
        Map<String, Object> map = new HashMap<>();
        map.put("rotation", indexService.getRotation());
        map.put("hotArticle", indexService.getHotArticleOnRight());
        map.put("hotLabel", indexService.getHotLabelOnRight());
        map.put("recommendArticle", indexService.getRecommendArticleOnRight());
        return ResponseData.success(map, "首页右边推荐及轮播图");
    }

    @ResponseBody
    @RequestMapping(value = "index/plate", method = RequestMethod.GET)
    public Map<String, Object> getPlateById(Plate plate) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("indexPlate", indexService.getPlateById(plate));
        map.put("plateChildren", indexService.getPlateChildren(plate.getId()));
        return ResponseData.success(map, "首页顶部板块");
    }

    @ResponseBody
    @RequestMapping(value = "index/plate/follow", method = RequestMethod.POST)
    public Map<String, Object> followPlate(@RequestBody Plate plate) {
        int num = indexService.saveFollowByPlateId(plate);

        if (num == -2) {
            return ResponseData.error("不存在此用户，请联系管理员");
        } else {
            return num > 0 ? ResponseData.success("success", "关注或取消关注板块成功") : ResponseData.error("网络故障，请稍后再试");
        }
    }

    @ResponseBody
    @RequestMapping(value = "index/user/signIn", method = RequestMethod.POST)
    public Map<String, Object> saveSign(@RequestBody Sign sign) throws ParseException {
        int num = indexService.saveSign(sign.getUsername());

        if (num == -1) {
            return ResponseData.error("您今天已经签到过，明日再来吧");
        } else {
            return num > 0 ? ResponseData.success("success", "签到成功") : ResponseData.error("网络故障，请稍后再试");
        }
    }

    @ResponseBody
    @RequestMapping(value = "index/user/signIn/status", method = RequestMethod.POST)
    public Map<String, Object> getSignInStatus(@RequestBody Sign sign) throws ParseException {
        return ResponseData.success(indexService.getSignInStatus(sign.getUsername()), "签到状态");
    }

}
