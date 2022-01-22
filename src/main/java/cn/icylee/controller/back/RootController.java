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
        Root root1 = rootService.saveRoot(root);
        return root1 != null ? ResponseData.success(root1, "添加成功") : ResponseData.error("已有此类");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateRoot(@RequestBody Root root) {
        Root root1 = rootService.updateRoot(root);
        return root1 != null ? ResponseData.success(root1, "修改成功") : ResponseData.error("已有此类");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRoot(@RequestBody Root root) {
        int num = rootService.deleteRoot(root.getId());

        if (num == 1) {
            return ResponseData.success("success", "删除成功");
        } else if (num == 0) {
            return ResponseData.error("网络故障");
        } else {
            return ResponseData.error("该类下有子类，不能删除");
        }
    }

}
