package cn.icylee.controller.back;

import cn.icylee.bean.Comment;
import cn.icylee.bean.TableParameter;
import cn.icylee.service.back.CommentService;
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
@RequestMapping("/adm/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showAllComment(TableParameter tableParameter) {
        tableParameter = commentService.setIdsTool(tableParameter);
        int total = commentService.getCommentTotal(tableParameter);
        List<Comment> commentList = commentService.getPageComment(tableParameter);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", commentList);
        return ResponseData.success(map, "评论列表");
    }

    @ResponseBody
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public Map<String, Object> updateStatus(@RequestBody Comment comment) {
        return commentService.updateStatus(comment) > -1 ? ResponseData.success("success", "修改成功") : ResponseData.error("网络故障");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map<String, Object> deleteComment(@RequestBody Comment comment) {
        int[] ids = commentService.deleteComment(comment.getId());
        return ids.length > 0 ? ResponseData.success(ids, "删除成功") : ResponseData.error("网络故障");
    }

}
