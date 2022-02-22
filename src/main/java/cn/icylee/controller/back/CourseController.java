package cn.icylee.controller.back;

import cn.icylee.bean.*;
import cn.icylee.service.back.ArticleService;
import cn.icylee.service.back.CourseService;
import cn.icylee.service.back.ModularService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adm/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    ModularService modularService;

    @Autowired
    ArticleService articleService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllCourse(TableParameter tableParameter) {
        List<LabelTree> modularTree = new ArrayList<>();

        LabelTree LabelTree = new LabelTree();
        LabelTree.setId(0);
        LabelTree.setLabel("所有模块");
        LabelTree.setChildren(modularService.getOptionModular(0));
        modularTree.add(LabelTree);

        List<LabelTree> labelTree = new ArrayList<>();

        LabelTree Tree = new LabelTree();
        Tree.setId(0);
        Tree.setLabel("所有标签");
        Tree.setChildren(articleService.getLabelTree());
        labelTree.add(Tree);

        tableParameter = articleService.setIdsTool(tableParameter);

        Map<String, Object> map = new HashMap<>();
        map.put("modularTree", modularTree);
        map.put("labelTree", labelTree);
        map.put("allModular", courseService.getAllModular());
        map.put("allRoot", articleService.getAllRoot());
        map.put("allCategory", articleService.getAllCategory());
        map.put("allLabel", articleService.getAllLabel());
        map.put("total", courseService.getCourseTotal(tableParameter));
        map.put("items", courseService.getPageCourse(tableParameter));

        return ResponseData.success(map, "课程列表");
    }

    @ResponseBody
    @RequestMapping(value = "label", method = RequestMethod.GET)
    public Map<String, Object> getAllLabel() {
        Map<String, Object> map = new HashMap<>();
        map.put("allLabel", articleService.getAllLabel());
        map.put("optionModular", modularService.getOptionModular(0));
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getCourseById(int id) {
        return ResponseData.success(courseService.getCourseById(id), "文章详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveCourse(@RequestBody Course course) {
        return courseService.saveCourse(course) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("课程名重复");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有该课程");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Course course) {
        return courseService.updateStatus(course) > 0 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteCourse(@RequestBody Course course) {
        return courseService.deleteCourse(course.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "course"), "上传成功");
    }

}
