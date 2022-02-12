package cn.icylee.service.back;

import cn.icylee.bean.Message;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface MessageService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getMessageTotal(TableParameter tableParameter);

    List<Message> getPageMessage(TableParameter tableParameter);

    int deleteMessage(int id);
    
}
