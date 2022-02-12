package cn.icylee.controller.front;

import cn.icylee.bean.Verify;
import cn.icylee.service.front.VerifyFService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
