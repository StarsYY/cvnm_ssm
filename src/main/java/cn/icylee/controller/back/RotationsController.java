package cn.icylee.controller.back;

import cn.icylee.bean.Rotations;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.RotationsService;
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
@RequestMapping("/adm/rotations")
public class RotationsController {

    @Autowired
    RotationsService rotationsService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllRotations(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", rotationsService.getRotationsTotal(tableParameter));
        map.put("items", rotationsService.getPageRotations(tableParameter));
        return ResponseData.success(map, "勋章列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveRotations(@RequestBody Rotations rotations) {
        Rotations rotations1 = rotationsService.saveRotations(rotations);
        return rotations1 != null ? ResponseData.success(rotations1, "添加成功") : ResponseData.error("该图片已存在");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateRotations(@RequestBody Rotations rotations) {
        return rotationsService.updateRotations(rotations) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该图片已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRotations(@RequestBody Rotations rotations) {
        return rotationsService.deleteRotations(rotations.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "rotations"), "上传");
    }
    
}
