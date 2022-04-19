package cn.icylee.controller.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.ArticleService;
import cn.icylee.service.back.PlateService;
import cn.icylee.service.front.CreateService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/create")
public class CreateController {

    @Autowired
    PlateService plateService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CreateService createService;

    @ResponseBody
    @RequestMapping("")
    public Map<String, Object> showCreate() {
        Map<String, Object> map = new HashMap<>();
        map.put("allPlate", plateService.getOptionPlate(0));
        map.put("allLabel", articleService.getLabelTree());
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "draft", method = RequestMethod.GET)
    public Map<String, Object> getArticleDraft(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("draft", createService.getArticleDraft(article.getAuthor()));
        return ResponseData.success(map, "文章草稿");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveArticle(@RequestBody Article article) {
        return articleService.saveArticle(article) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此篇文章");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(int id) {
        return ResponseData.success(articleService.getArticleById(id), "文章详情");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此篇文章");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "article"), "上传成功");
    }

}
