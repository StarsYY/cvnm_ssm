package cn.icylee.service.front;

import cn.icylee.bean.Comment;
import cn.icylee.bean.Discuss;
import cn.icylee.bean.User;

public interface SendMessageService {

    int saveMessageFromComment(Comment comment);

    int saveMessageFromDiscuss(Discuss discuss);

    int saveMessageFromLogin(User user);

    int saveMessageFromMedal(int uid, String medal);

}
