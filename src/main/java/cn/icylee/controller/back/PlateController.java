package cn.icylee.controller.back;

import cn.icylee.bean.LabelTree;
import cn.icylee.bean.Plate;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.PlateService;
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
@RequestMapping("/adm/plate")
public class PlateController {

    @Autowired
    PlateService plateService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllPlate(TableParameter tableParameter) {
        int total = plateService.getPlateTotal(tableParameter);
        List<Plate> plateList = plateService.getPagePlate(tableParameter);
        List<LabelTree> optionsPlate = plateService.getOptionPlate(0);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", plateList);
        map.put("optionPlate", optionsPlate);
        return ResponseData.success(map, "板块类别列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> savePlate(@RequestBody Plate plate) {
        return plateService.savePlate(plate) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("该板块已存在");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updatePlate(@RequestBody Plate plate) {
        return plateService.updatePlate(plate) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该板块已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deletePlate(@RequestBody Plate plate) {
        return plateService.deletePlate(plate.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

}