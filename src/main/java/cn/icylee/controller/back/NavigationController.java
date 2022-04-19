package cn.icylee.controller.back;

import cn.icylee.bean.Navigation;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.NavigationService;
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
@RequestMapping("/adm/navigation")
public class NavigationController {

    @Autowired
    NavigationService navigationService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllNavigation(TableParameter tableParameter) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", navigationService.getNavigationTotal(tableParameter));
        map.put("items", navigationService.getPageNavigation(tableParameter));
        return ResponseData.success(map, "首页顶部导航列表");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveNavigation(@RequestBody Navigation navigation) {
        Navigation navigation1 = navigationService.saveNavigation(navigation);
        return navigation1 != null ? ResponseData.success(navigation1, "添加成功") : ResponseData.error("该内容已存在");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateNavigation(@RequestBody Navigation navigation) {
        return navigationService.updateNavigation(navigation) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("该内容已存在");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteNavigation(@RequestBody Navigation navigation) {
        return navigationService.deleteNavigation(navigation.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
