package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.MessageMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (User user : userList) {
                    ids.append(user.getUid()).append(",");
                }
                tableParameter.setIds(ids.toString());
            }
        }
        if (tableParameter.getLabel() != null && !tableParameter.getLabel().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getLabel() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder cids = new StringBuilder();
                for (User user : userList) {
                    cids.append(user.getUid()).append(",");
                }
                tableParameter.setCids(cids.toString());
            }
        }
        return tableParameter;
    }

    @Override
    public int getMessageTotal(TableParameter tableParameter) {
        return messageMapper.getMessageTotal(tableParameter);
    }

    @Override
    public List<Message> getPageMessage(TableParameter tableParameter) {
        List<Message> messageList = messageMapper.getMessageList(tableParameter);
        for (Message message : messageList) {
            message.setSendername(userMapper.selectByPrimaryKey(message.getSenderuid()).getNickname());
            message.setReceivename(userMapper.selectByPrimaryKey(message.getReceiveuid()).getNickname());
        }
        return messageList;
    }

    @Override
    public int deleteMessage(int id) {
        return messageMapper.deleteByPrimaryKey(id);
    }
}
