package cn.icylee.controller.back;

import cn.icylee.bean.Discuss;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.DiscussService;
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
@RequestMapping("/adm/discuss")
public class DiscussController {

    @Autowired
    DiscussService discussService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllDiscuss(TableParameter tableParameter) {
        tableParameter = discussService.setIdsTool(tableParameter);
        int total = discussService.getDiscussTotal(tableParameter);
        List<Discuss> discussList = discussService.getPageDiscuss(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", discussList);
        return ResponseData.success(map, "评论列表");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Discuss discuss) {
        return discussService.updateStatus(discuss) > -1 ? ResponseData.success("success", "修改成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteDiscuss(@RequestBody Discuss discuss) {
        int[] ids = discussService.deleteDiscuss(discuss.getId());
        return ids.length > 0 ? ResponseData.success(ids, "删除成功") : ResponseData.error("网络故障");
    }
    
}
