package cn.icylee.controller.back;

import cn.icylee.bean.Root;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.RootService;
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
@RequestMapping("/adm/root")
public class RootController {

    @Autowired
    RootService rootService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllRoot(TableParameter tableParameter) {
        int total = rootService.getRootTotal(tableParameter);
        List<Root> rootList = rootService.getPageRoot(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", rootList);
        return ResponseData.success(map, "标签类别列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveRoot(@RequestBody Root root) {
        return rootService.saveRoot(root) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateRoot(@RequestBody Root root) {
        return rootService.updateRoot(root) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRoot(@RequestBody Root root) {
        return rootService.deleteRoot(root.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

}
