package cn.icylee.controller.back;

import cn.icylee.bean.Admin;
import cn.icylee.service.back.AdminService;
import cn.icylee.utils.ResponseData;
import cn.icylee.bean.TableParameter;
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
@RequestMapping("/adm/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllAdmin(TableParameter tableParameter) {
        int total = adminService.getAdminTotal(tableParameter);
        List<Admin> adminList = adminService.getPageAdmin(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", adminList);
        return ResponseData.success(map, "管理员列表");
    }

    @ResponseBody
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Map<String, Object> getAdminById(int id) {
        return ResponseData.success(adminService.getAdminById(id), "管理员详情");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Map<String, Object> saveAdmin(@RequestBody Admin admin) {
        return adminService.saveAdmin(admin) > 0 ? ResponseData.success("success", "添加成功") : ResponseData.error("已有此管理员");
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin) > 0 ? ResponseData.success("success", "修改成功") : ResponseData.error("已有此管理员");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Admin admin) {
        return adminService.updateStatus(admin) > -1 ? ResponseData.success("success", "更改成功") : null;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteAdmin(@RequestBody Admin admin) {
        return adminService.deleteAdmin(admin.getId()) > 0 ? ResponseData.success("success", "删除成功") : null;
    }

}
