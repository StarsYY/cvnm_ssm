package cn.icylee.controller.back;

import cn.icylee.bean.Sign;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.SignService;
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
@RequestMapping("/adm/sign")
public class SignController {

    @Autowired
    SignService signService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllSign(TableParameter tableParameter) {
        tableParameter = signService.setIdsTool(tableParameter);
        int total = signService.getSignTotal(tableParameter);
        List<Sign> signList = signService.getPageSign(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", signList);
        return ResponseData.success(map, "签到列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteSign(@RequestBody Sign sign) {
        return signService.deleteSign(sign.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
