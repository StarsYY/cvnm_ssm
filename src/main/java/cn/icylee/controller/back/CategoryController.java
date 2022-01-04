package cn.icylee.controller.back;

import cn.icylee.bean.Category;
import cn.icylee.bean.Root;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.CategoryService;
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
@RequestMapping("/adm/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllCategory(TableParameter tableParameter) {
        int total = categoryService.getCategoryTotal(tableParameter);
        List<Category> categoryList = categoryService.getPageCategory(tableParameter);
        Map<String, Object> rootList = categoryService.getAllRoot();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", categoryList);
        map.put("allRoot", rootList);
        return ResponseData.success(map, "标签类别列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此标签");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteCategory(@RequestBody Category category) {
        return categoryService.deleteCategory(category.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

}
