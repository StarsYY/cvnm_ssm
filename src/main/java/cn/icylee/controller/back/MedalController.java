package cn.icylee.controller.back;

import cn.icylee.bean.LabelTree;
import cn.icylee.bean.Medal;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.MedalService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adm/medal")
public class MedalController {

    @Autowired
    MedalService medalService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllMedal(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", medalService.getMedalTotal(tableParameter));
        map.put("items", medalService.getPageMedal(tableParameter));
        return ResponseData.success(map, "勋章列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveMedal(@RequestBody Medal medal) {
        Medal medal1 = medalService.saveMedal(medal);
        return medal1 != null ? ResponseData.success(medal1, "添加成功") : ResponseData.error("该勋章已存在");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateMedal(@RequestBody Medal medal) {
        return medalService.updateMedal(medal) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该勋章已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMedal(@RequestBody Medal medal) {
        int num = medalService.deleteMedal(medal.getId());
        if (num == -1) {
            return ResponseData.error("存在用户拥有此勋章，不能删除！");
        }
        return num > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "medal"), "上传");
    }
    
}
