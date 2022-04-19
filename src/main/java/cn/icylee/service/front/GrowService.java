package cn.icylee.service.front;

public interface GrowService {

    int updateIncreaseIntegralAndGrowFromFollow(int uid);

    int updateIncreaseIntegralAndGrowFromPrefer(int uid);

    int updateIncreaseIntegralAndGrowFromArticleOrCourse(int uid);

    int updateIncreaseIntegralAndGrowFromCommentOrDiscuss(int uid);

    int updateIncreaseIntegralAndGrowFromSign(int uid);

    int updateDecreaseIntegralAndGrowFromFollow(int uid);

    int updateDecreaseIntegralAndGrowFromPrefer(int uid);

    int updateDecreaseIntegralAndGrowFromArticleOrCourse(int uid);

    int updateDecreaseIntegralAndGrowFromCommentOrDiscuss(int uid);

}
