package cn.icylee.controller.back;

import cn.icylee.bean.Rotation;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.RotationService;
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
@RequestMapping("/adm/rotation")
public class RotationController {

    @Autowired
    RotationService rotationService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllRotation(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", rotationService.getRotationTotal(tableParameter));
        map.put("items", rotationService.getPageRotation(tableParameter));
        return ResponseData.success(map, "勋章列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveRotation(@RequestBody Rotation rotation) {
        Rotation rotation1 = rotationService.saveRotation(rotation);
        return rotation1 != null ? ResponseData.success(rotation1, "添加成功") : ResponseData.error("该图片已存在");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateRotation(@RequestBody Rotation rotation) {
        return rotationService.updateRotation(rotation) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该图片已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRotation(@RequestBody Rotation rotation) {
        return rotationService.deleteRotation(rotation.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "rotation"), "上传");
    }
    
}
