package cn.icylee.controller.back;

import cn.icylee.bean.Video;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.VideoService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adm/video")
public class VideoController {

    @Autowired
    VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllVideo(TableParameter tableParameter) {
        tableParameter = videoService.setIdsTool(tableParameter);
        int total = videoService.getVideoTotal(tableParameter);
        List<Video> videoList = videoService.getPageVideo(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", videoList);
        return ResponseData.success(map, "视频列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteVideo(@RequestBody Video video) {
        return videoService.deleteVideo(video.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
