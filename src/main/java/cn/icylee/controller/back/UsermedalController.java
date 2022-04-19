package cn.icylee.controller.back;

import cn.icylee.bean.Usermedal;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.UsermedalService;
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
@RequestMapping("/adm/usermedal")
public class UsermedalController {

    @Autowired
    UsermedalService usermedalService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllUsermedal(TableParameter tableParameter) {
        tableParameter = usermedalService.setIdsTool(tableParameter);
        int total = usermedalService.getUsermedalTotal(tableParameter);
        List<Usermedal> usermedalList = usermedalService.getPageUsermedal(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", usermedalList);
        return ResponseData.success(map, "用户勋章列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteUsermedal(@RequestBody Usermedal usermedal) {
        return usermedalService.deleteUsermedal(usermedal.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
