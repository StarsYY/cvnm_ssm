package cn.icylee.controller.back;

import cn.icylee.bean.Modular;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.ModularService;
import cn.icylee.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/adm/modular")
public class ModularController {
    
    @Autowired
    ModularService modularService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllModular(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", modularService.getModularTotal(tableParameter));
        map.put("items", modularService.getPageModular(tableParameter));
        map.put("optionModular", modularService.getOptionModular(0));
        return ResponseData.success(map, "板块类别列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveModular(@RequestBody Modular modular) {
        Modular modular1 = modularService.saveModular(modular);
        if (modular1 != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("modular", modular1);
            map.put("optionModular", modularService.getOptionModular(0));

            return ResponseData.success(map, "添加成功");
        } else {
            return ResponseData.error("该板块已存在");
        }
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateModular(@RequestBody Modular modular) {
        return modularService.updateModular(modular) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该板块已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteModular(@RequestBody Modular modular) {
        int num = modularService.deleteModular(modular.getId());

        if (num == 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("optionModular", modularService.getOptionModular(0));

            return ResponseData.success(map, "删除成功");
        } else if (num == 0) {
            return ResponseData.error("网络故障");
        } else if (num == -1){
            return ResponseData.error("有模块继承该模块，不能删除");
        } else {
            return ResponseData.error("该模块下有课程，不能删除");
        }
    }
    
}
