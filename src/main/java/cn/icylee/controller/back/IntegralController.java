package cn.icylee.controller.back;

import cn.icylee.bean.Integral;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.IntegralService;
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
@RequestMapping("/adm/integral")
public class IntegralController {

    @Autowired
    IntegralService integralService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllIntegral(TableParameter tableParameter) {
        tableParameter = integralService.setIdsTool(tableParameter);
        int total = integralService.getIntegralTotal(tableParameter);
        List<Integral> integralList = integralService.getPageIntegral(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", integralList);
        return ResponseData.success(map, "评论列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteIntegral(@RequestBody Integral integral) {
        return integralService.deleteIntegral(integral.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
