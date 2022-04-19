package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.SendMessageService;
import cn.icylee.service.front.UserMedalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMedalServiceImpl implements UserMedalService {

    @Autowired
    MedalMapper medalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UsermedalMapper usermedalMapper;

    @Autowired
    SendMessageService sendMessageService;

    private int save(int num, int uid, int medalId, String medal) {
        if (num == 0) {
            Usermedal usermedal = new Usermedal();
            usermedal.setUserid(uid);
            usermedal.setMedalid(medalId);
            usermedal.setCreatetime(new Date());

            if (usermedalMapper.insert(usermedal) > 0) {
                return sendMessageService.saveMessageFromMedal(uid, medal);
            } else {
                return 0;
            }
        }
        return 1;
    }

    private int delete(int num, UsermedalExample usermedalExample) {
        if (num > 0) {
            return usermedalMapper.deleteByExample(usermedalExample);
        }
        return 1;
    }

    @Override
    public int saveUserMedal(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        List<Medal> medalList = medalMapper.selectByExample(null);

        int i = 0;
        for (Medal medal : medalList) {
            UsermedalExample usermedalExample = new UsermedalExample();
            usermedalExample.createCriteria().andUseridEqualTo(uid).andMedalidEqualTo(medal.getId());
            int num = usermedalMapper.countByExample(usermedalExample);

            if (i == 0) {
                FollowExample followUser = new FollowExample();
                followUser.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("user");

                if (followMapper.countByExample(followUser) >= 1) {
                    if (save(num, uid, medal.getId(), medal.getName()) == 0)
                        return 0;
                } else {
                    if (delete(num, usermedalExample) == 0)
                        return 0;
                }
            }

            if (i == 1) {
                FollowExample fansUser = new FollowExample();
                fansUser.createCriteria().andDatasourceEqualTo("user").andDataidEqualTo(uid);

                if (followMapper.countByExample(fansUser) >= 1) {
                    if (save(num, uid, medal.getId(), medal.getName()) == 0)
                        return 0;
                } else {
                    if (delete(num, usermedalExample) == 0)
                        return 0;
                }
            }

            if (i == 2) {
                if (articleMapper.getWatch(uid) > 50) {
                    if (save(num, uid, medal.getId(), medal.getName()) == 0)
                        return 0;
                } else {
                    if (delete(num, usermedalExample) == 0)
                        return 0;
                }
            }

            if (i == 3) {
                ArticleExample articleExample = new ArticleExample();
                articleExample.createCriteria().andUseridEqualTo(uid)
                        .andPublishEqualTo("公开")
                        .andStatusEqualTo("已发布")
                        .andIsdelEqualTo(0)
                        .andTagEqualTo("精华");
                if (articleMapper.countByExample(articleExample) >= 1) {
                    if (save(num, uid, medal.getId(), medal.getName()) == 0)
                        return 0;
                } else {
                    if (delete(num, usermedalExample) == 0)
                        return 0;
                }
            }

            if (i == 4) {
                if (followMapper.getFollowByUid(uid) >= 1) {
                    if (save(num, uid, medal.getId(), medal.getName()) == 0)
                        return 0;
                } else {
                    if (delete(num, usermedalExample) == 0)
                        return 0;
                }
            }

            i++;
        }
        return 1;
    }
}
