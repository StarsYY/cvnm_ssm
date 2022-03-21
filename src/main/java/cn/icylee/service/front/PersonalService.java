package cn.icylee.service.front;

import cn.icylee.bean.*;

import java.util.List;
import java.util.Map;

public interface PersonalService {

    User getUser(String username, String loginName);

    Map<String, Integer> getCommunication(String username);

    List<Article> getNewArticle(String username);

    List<User> getFollow(String username);

    List<User> getFans(String username);

    List<Article> getCollect(String username);

    List<Article> getMyArticle(String username, String loginName);

    List<User> getMyFans(String username, String loginName);

    List<User> getMyFollow(String username, String loginName);

    Verify getVerify(String username);

    List<Integral> getIntegral(String username, Integer page);

    Integer getIntegralTotal(String username);

    List<Article> getMyDraft(String username);

    List<Comment> getMyComment(String username);

}
