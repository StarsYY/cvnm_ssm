package cn.icylee.controller.back;

import cn.icylee.bean.Label;
import cn.icylee.bean.LabelTree;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.LabelService;
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
@RequestMapping("/adm/label")
public class LabelController {

    @Autowired
    LabelService labelService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllLabel(TableParameter tableParameter) {
        List<LabelTree> labelTree = labelService.getCategoryTree();
        int total = labelService.getLabelTotal(tableParameter);
        Map<String, Object> labelList = labelService.getPageLabel(tableParameter);
        List<Object> rootList = labelService.getAllRoot();
        Map<String, Object> map = new HashMap<>();
        map.put("labelTree", labelTree);
        map.put("total", total);
        map.put("allCategory", labelList.get("allCategory"));
        map.put("allRoot", rootList);
        map.put("items", labelList.get("labelList"));
        return ResponseData.success(map, "标签列表");
    }

    @ResponseBody
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public Map<String, Object> getAllCategory() {
        Map<String, Object> map = new HashMap<>();
        map.put("allCategory", labelService.getAllCategory());
        return ResponseData.success(map, "列表类别");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getLabelById(int id) {
        return ResponseData.success(labelService.getLabelById(id), "标签详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveLabel(@RequestBody Label label) {
        return labelService.saveLabel(label) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateLabel(@RequestBody Label label) {
        return labelService.updateLabel(label) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteLabel(@RequestBody Label label) {
        return labelService.deleteLabel(label.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String serverName = "http://" + request.getServerName() + ":" + request.getServerPort();
        String diskPath = "E:\\IDEA\\ideaWeb";
        String imagePath = "/upload/image/label/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
        String imageName = UUID.randomUUID().toString() + ".png";
        map.put("imagePath", serverName + imagePath + imageName);
        return UploadFile.base64StringToImage(upload.getBase64(), diskPath + imagePath, imageName) ? ResponseData.success(map, "上传成功") : null;
    }

}
