package cn.icylee.controller.front;

import cn.icylee.bean.Comment;
import cn.icylee.bean.Report;
import cn.icylee.bean.Upload;
import cn.icylee.service.front.DetailService;
import cn.icylee.service.front.UserMedalService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/detail")
public class DetailController {

    @Autowired
    DetailService detailService;

    @Autowired
    UserMedalService userMedalService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", detailService.getArticleById(comment));
        if (userMedalService.saveUserMedal(detailService.getUsernameTool(comment.getArticleid())) > 0) {
            map.put("userMedal", detailService.getUserMedal(detailService.getUidTool(comment.getArticleid())));
        }
        return detailService.updateArticleWatch(comment.getArticleid()) > 0 ? ResponseData.success(map, "文章详情") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "left", method = RequestMethod.GET)
    public Map<String, Object> getLeft(int uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("leftUserHotArticle", detailService.getHotArticleByUid(uid));
        map.put("leftUserNewArticle", detailService.getNewArticleByUid(uid));
        return ResponseData.success(map, "用户热门，最新文章");
    }

    @ResponseBody
    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public Map<String, Object> getAllComment(Comment comment) {
        return ResponseData.success(detailService.saveGetAllComment(comment), "文章评论");
    }

    @ResponseBody
    @RequestMapping(value = "comment/add", method = RequestMethod.POST)
    public Map<String, Object> saveComment(@RequestBody Comment comment) {
        int num = detailService.saveComment(comment);

        if (num == -1) {
            return ResponseData.error("您已经对此文章评论过该内容，不能重复提交。");
        }
        return num > 0 ? ResponseData.success("success", "评论成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "upOrDown", method = RequestMethod.POST)
    public Map<String, Object> upOrDown(@RequestBody Comment comment) {
        return detailService.savePreferUpOrDownArticle(comment) > 0 ? ResponseData.success("success", "点赞或点踩成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "upComment", method = RequestMethod.POST)
    public Map<String, Object> upComment(@RequestBody Comment comment) {
        Map<String, Object> map = new HashMap<>();
        int success = detailService.savePreferUpArticleComment(comment);
        map.put("commentList", detailService.saveGetAllComment(comment));
        return success > 0 ? ResponseData.success(map, "点赞评论成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "star", method = RequestMethod.POST)
    public Map<String, Object> star(@RequestBody Comment comment) {
        return detailService.saveFollowStarArticle(comment) > 0 ? ResponseData.success("success", "收藏或取消收藏成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "followAuthor", method = RequestMethod.POST)
    public Map<String, Object> followAuthor(@RequestBody Comment comment) {
        Map<String, Object> map = new HashMap<>();
        int success = detailService.saveFollowAuthor(comment);
        map.put("commentList", detailService.saveGetAllComment(comment));
        return success > 0 ? ResponseData.success(map, "关注或取消关注成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "report", method = RequestMethod.POST)
    public Map<String, Object> reportArticleOrComment(@RequestBody Report report) {
        return detailService.saveReport(report) > 0 ? ResponseData.success("success", "举报成功") : ResponseData.error("欸哎，我堵在这了つ ◕_◕ ༽つ");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String serverName = "http://" + request.getServerName() + ":" + request.getServerPort();
        String diskPath = "E:\\IDEA\\ideaWeb";
        String imagePath = "/upload/image/comment/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
        String imageName = UUID.randomUUID().toString() + ".png";
        map.put("imagePath", serverName + imagePath + imageName);
        return UploadFile.base64StringToImage(upload.getBase64(), diskPath + imagePath, imageName) ? ResponseData.success(map, "上传成功") : null;
    }

}
