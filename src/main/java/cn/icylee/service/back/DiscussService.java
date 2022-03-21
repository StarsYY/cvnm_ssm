package cn.icylee.service.back;

import cn.icylee.bean.Discuss;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface DiscussService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getDiscussTotal(TableParameter tableParameter);

    List<Discuss> getPageDiscuss(TableParameter tableParameter);

    int updateStatus(Discuss discuss);

    StringBuilder setIds(int id, StringBuilder ids);

    int[] deleteDiscuss(int id);
    
}
