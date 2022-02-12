package cn.icylee.controller.back;

import cn.icylee.bean.Verify;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.VerifyService;
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
@RequestMapping("/adm/verify")
public class VerifyController {

    @Autowired
    VerifyService verifyService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllVerify(TableParameter tableParameter) {
        tableParameter = verifyService.setIdsTool(tableParameter);
        int total = verifyService.getVerifyTotal(tableParameter);
        List<Verify> verifyList = verifyService.getPageVerify(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", verifyList);
        return ResponseData.success(map, "管理员列表");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Verify verify) {
        return verifyService.updateStatus(verify) > -1 ? ResponseData.success("success", "更改成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteVerify(@RequestBody Verify verify) {
        return verifyService.deleteVerify(verify.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
