package cn.icylee.service.front.impl;

import cn.icylee.bean.Comment;
import cn.icylee.bean.Discuss;
import cn.icylee.bean.Message;
import cn.icylee.bean.User;
import cn.icylee.dao.*;
import cn.icylee.service.front.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    DiscussMapper discussMapper;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public int saveMessageFromComment(Comment comment) {
        Message message = new Message();

        message.setSenderuid(comment.getUserid());

        if (comment.getComid() > 0) {
            message.setReceiveuid(commentMapper.selectByPrimaryKey(comment.getComid()).getUserid());
            message.setDatasource("comment");
            message.setAddition(comment.getComid());
        } else {
            message.setReceiveuid(articleMapper.selectByPrimaryKey(comment.getArticleid()).getUserid());
            message.setDatasource("article");
            message.setAddition(comment.getArticleid());
        }

        message.setContent(String.valueOf(comment.getId()));
        message.setRead(0);
        message.setType(0);
        message.setCreatetime(new Date());

        return messageMapper.insert(message);

    }

    @Override
    public int saveMessageFromDiscuss(Discuss discuss) {
        Message message = new Message();

        message.setSenderuid(discuss.getUserid());

        if (discuss.getDisid() > 0) {
            message.setReceiveuid(discussMapper.selectByPrimaryKey(discuss.getDisid()).getUserid());
            message.setDatasource("discuss");
            message.setAddition(discuss.getDisid());
        } else {
            message.setReceiveuid(courseMapper.selectByPrimaryKey(discuss.getCourseid()).getUserid());
            message.setDatasource("course");
            message.setAddition(discuss.getCourseid());
        }

        message.setContent(String.valueOf(discuss.getId()));
        message.setRead(0);
        message.setType(0);
        message.setCreatetime(new Date());

        return messageMapper.insert(message);
    }

    @Override
    public int saveMessageFromLogin(User user) {
        Message message = new Message();

        message.setReceiveuid(user.getUid());
        message.setContent("您好！您已经注册成为开发者论坛的会员，请您在发表言论时，遵守当地法律法规。如果您有什么疑问可以联系管理员。");
        message.setRead(0);
        message.setType(1);
        message.setCreatetime(new Date());

        return messageMapper.insert(message);
    }

    @Override
    public int saveMessageFromMedal(int uid, String medal) {
        Message message = new Message();

        message.setReceiveuid(uid);
        message.setContent("尊敬的开发者，您好！恭喜您获得 论坛" + medal + " 勋章！");
        message.setRead(0);
        message.setType(1);
        message.setCreatetime(new Date());

        return messageMapper.insert(message);
    }

}
