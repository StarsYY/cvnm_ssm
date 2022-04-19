package cn.icylee.service.front;

import cn.icylee.bean.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface PersonalService {

    User getUser(String username, String loginName);

    List<Medal> getUserMedal(String username);

    List<Medal> getAllMedal();

    int updateUserSummary(User user);

    Map<String, Integer> getCommunication(String username);

    List<Article> getNewArticle(String username);

    List<User> getFollow(String username);

    List<User> getExpert(String username);

    List<User> getFans(String username);

    List<Article> getCollect(Index index);

    List<Message> getMySystemMessage(Index index);

    List<Message> getMyAdministratorMessage(Index index);

    int updateMessage(Message message);

    int updateMessageAll(Message message);

    int deleteSelectMessage(int[] ids);

    List<Article> getMyArticle(Index index);

    int deleteMyArticle(int id);

    List<User> getMyFans(Index index);

    List<User> getMyFollow(Index index);

    List<Plate> getMyFollowPlate(Index index) throws ParseException;

    List<Label> getMyFollowLabel(Index index) throws ParseException;

    Verify getVerify(String username);

    List<Integral> getIntegral(String username, Integer page);

    Integer getIntegralTotal(String username);

    List<Article> getMyDraft(Index index);

    int deleteMyDraft(int id);

    List<Article> getMyAudit(Index index);

    int deleteMyAudit(int id);

    List<Comment> getMyComment(Index index);

    int[] deleteMyComment(int id);

}
