package cn.icylee.controller.front;

import cn.icylee.bean.Comment;
import cn.icylee.bean.Upload;
import cn.icylee.service.front.DetailService;
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

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(int id) {
        return ResponseData.success(detailService.getArticleById(id), "文章详情");
    }

    @ResponseBody
    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public Map<String, Object> getAllComment(int id) {
        return ResponseData.success(detailService.getAllComment(id), "文章评论");
    }

    @ResponseBody
    @RequestMapping(value = "comment/add", method = RequestMethod.POST)
    public Map<String, Object> saveComment(@RequestBody Comment comment) {
        Map<String, Object> map = new HashMap<>();
        int success = detailService.saveComment(comment);
        map.put("commentList", detailService.getAllComment(comment.getArticleid()));
        return success > 0 ? ResponseData.success(map, "评论成功") : ResponseData.error("欸哎，我都在这了つ ◕_◕ ༽つ");
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
