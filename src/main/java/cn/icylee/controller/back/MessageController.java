package cn.icylee.controller.back;

import cn.icylee.bean.Message;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.MessageService;
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
@RequestMapping("/adm/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllMessage(TableParameter tableParameter) {
        tableParameter = messageService.setIdsTool(tableParameter);
        int total = messageService.getMessageTotal(tableParameter);
        List<Message> messageList = messageService.getPageMessage(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", messageList);
        return ResponseData.success(map, "评论列表");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteMessage(@RequestBody Message message) {
        return messageService.deleteMessage(message.getId()) > 0 ? ResponseData.success("success", "删除成功") : ResponseData.error("网络故障");
    }
    
}
