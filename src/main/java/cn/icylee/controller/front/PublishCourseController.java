package cn.icylee.controller.front;

import cn.icylee.bean.Article;
import cn.icylee.bean.Course;
import cn.icylee.bean.Upload;
import cn.icylee.bean.Video;
import cn.icylee.service.back.ArticleService;
import cn.icylee.service.back.CourseService;
import cn.icylee.service.back.ModularService;
import cn.icylee.service.back.VideoService;
import cn.icylee.service.front.PublishCourseService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/create/course")
public class PublishCourseController {

    @Autowired
    ModularService modularService;

    @Autowired
    ArticleService articleService;

    @Autowired
    PublishCourseService publishCourseService;

    @Autowired
    CourseService courseService;

    @Autowired
    VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> show() {
        Map<String, Object> map = new HashMap<>();
        map.put("optionModular", modularService.getOptionModular(0));
        map.put("allLabel", articleService.getLabelTree());
        return ResponseData.success(map, "所有标签");
    }

    @ResponseBody
    @RequestMapping(value = "draft", method = RequestMethod.GET)
    public Map<String, Object> getCourseDraft(Course course) {
        Map<String, Object> map = new HashMap<>();
        map.put("draft", publishCourseService.getCourseDraft(course.getAuthor()));
        return ResponseData.success(map, "课程草稿");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveCourse(@RequestBody Course course) throws IOException {
        int id = courseService.saveCourse(course);
        if (id > 0) {
            ObjectMapper mapper = new ObjectMapper();
            List<Video> videoList = mapper.readValue(course.getVideo(), new TypeReference<List<Video>>(){});
            return videoService.saveVideo(videoList, id) ? ResponseData.success("success", "添加成功") : ResponseData.error("网络故障");
        }
        return ResponseData.error("课程名重复");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getCourseById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("course", courseService.getCourseById(id));
        return ResponseData.success(map, "课程详情");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateCourse(@RequestBody Course course) throws IOException {
        return courseService.updateCourse(course) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有该课程");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "course"), "上传成功");
    }

}
