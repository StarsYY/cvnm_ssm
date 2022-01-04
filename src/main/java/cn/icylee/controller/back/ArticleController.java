package cn.icylee.controller.back;

import cn.icylee.bean.*;
import cn.icylee.service.back.ArticleService;
import cn.icylee.service.back.PlateService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/adm/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    PlateService plateService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllArticle(TableParameter tableParameter) {
        tableParameter = articleService.setIdsTool(tableParameter);
        List<LabelTree> plateTree = new ArrayList<>();
        LabelTree LabelTree = new LabelTree();
        LabelTree.setId(0);
        LabelTree.setLabel("所有板块");
        LabelTree.setChildren(plateService.getOptionPlate(0));
        plateTree.add(LabelTree);
        Map<String, Object> articleList = articleService.getPageArticle(tableParameter);
        int total = articleService.getArticleTotal(tableParameter);
        List<LabelTree> list = new ArrayList<>();
        LabelTree Tree = new LabelTree();
        Tree.setId(0);
        Tree.setLabel("所有标签");
        Tree.setChildren(articleService.getLabelTree());
        list.add(Tree);
        Map<String, Object> map = new HashMap<>();
        map.put("plateTree", plateTree);
        map.put("labelTree", list);
        map.put("total", total);
        map.put("allRoot", articleService.getAllRoot());
        map.put("allCategory", articleList.get("allCategory"));
        map.put("allLabel", articleList.get("allLabel"));
        map.put("allPlate", articleService.getAllPlate());
        map.put("items", articleList.get("articleList"));
        return ResponseData.success(map, "文章列表");
    }

    @ResponseBody
    @RequestMapping(value = "label", method = RequestMethod.GET)
    public Map<String, Object> getAllLabel() {
        List<LabelTree> optionsPlate = plateService.getOptionPlate(0);
        Map<String, Object> map = new HashMap<>();
        map.put("allCategory", articleService.getAllCategory());
        map.put("allLabel", articleService.getAllLabel());
        map.put("optionPlate", optionsPlate);
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "searchUser", method = RequestMethod.GET)
    public Map<String, Object> searchUser(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("userList", articleService.searchUser(name));
        return ResponseData.success(map, "用户列表");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(int id) {
        return ResponseData.success(articleService.getArticleById(id), "文章详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveArticle(@RequestBody Article article) {
        return articleService.saveArticle(article) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此篇文章");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此篇文章");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Article article) {
        return articleService.updateStatus(article) > 0 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "changeTag", method = RequestMethod.POST)
    public Map<String, Object> updateTag(@RequestBody Article article) {
        return articleService.updateTag(article) > 0 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "changeRTag", method = RequestMethod.POST)
    public Map<String, Object> updateRTag(@RequestBody Article article) {
        return articleService.updateRTag(article) > 0 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteArticle(@RequestBody Article article) {
        return articleService.deleteArticle(article.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String serverName = "http://" + request.getServerName() + ":" + request.getServerPort();
        String diskPath = "E:\\IDEA\\ideaWeb";
        String imagePath = "/upload/image/article/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
        String imageName = UUID.randomUUID().toString() + ".png";
        map.put("imagePath", serverName + imagePath + imageName);
        return UploadFile.base64StringToImage(upload.getBase64(), diskPath + imagePath, imageName) ? ResponseData.success(map, "上传成功") : null;
    }

}
