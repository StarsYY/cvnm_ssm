package cn.icylee.controller.back;

import cn.icylee.bean.Label;
import cn.icylee.bean.LabelTree;
import cn.icylee.bean.TableParameter;
import cn.icylee.bean.Upload;
import cn.icylee.service.back.LabelService;
import cn.icylee.utils.ResponseData;
import cn.icylee.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/adm/label")
public class LabelController {

    @Autowired
    LabelService labelService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllLabel(TableParameter tableParameter) {
        List<LabelTree> labelTree = labelService.getCategoryTree();
        int total = labelService.getLabelTotal(tableParameter);
        Map<String, Object> labelList = labelService.getPageLabel(tableParameter);
        List<Object> rootList = labelService.getAllRoot();
        Map<String, Object> map = new HashMap<>();
        map.put("labelTree", labelTree);
        map.put("total", total);
        map.put("allCategory", labelList.get("allCategory"));
        map.put("allRoot", rootList);
        map.put("items", labelList.get("labelList"));
        return ResponseData.success(map, "标签列表");
    }

    @ResponseBody
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public Map<String, Object> getAllCategory() {
        Map<String, Object> map = new HashMap<>();
        map.put("allCategory", labelService.getAllCategory());
        return ResponseData.success(map, "列表类别");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getLabelById(int id) {
        return ResponseData.success(labelService.getLabelById(id), "标签详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveLabel(@RequestBody Label label) {
        Label label1 = labelService.saveLabel(label);
        return label1 != null ? ResponseData.success(label1, "添加成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateLabel(@RequestBody Label label) {
        Label label1 = labelService.updateLabel(label);
        return label1 != null ? ResponseData.success(label1, "修改成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteLabel(@RequestBody Label label) {
        int num = labelService.deleteLabel(label.getId());

        if (num == 1) {
            return ResponseData.success("success", "删除成功");
        } else if (num == 0) {
            return ResponseData.error("网络故障");
        } else {
            return ResponseData.error("该标签下还有文章，不能删除");
        }
    }

}
