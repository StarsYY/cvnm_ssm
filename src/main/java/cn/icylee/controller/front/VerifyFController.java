package cn.icylee.controller.front;

import cn.icylee.bean.Upload;
import cn.icylee.bean.Verify;
import cn.icylee.service.front.VerifyFService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/verify")
public class VerifyFController {

    @Autowired
    VerifyFService verifyFService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> getVerifyByUid(int uid) {
        return ResponseData.success(verifyFService.getVerifyByUid(uid), "认证信息");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveVerify(@RequestBody Verify verify) {
        return verifyFService.saveVerify(verify) > 0 ? ResponseData.success("success", "认证") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Upload upload, HttpServletRequest request) {
        return ResponseData.success(UploadFile.uploadImage(upload, request, "verify"), "上传成功");
    }

}
