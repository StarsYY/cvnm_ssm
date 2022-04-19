package cn.icylee.service.front.impl;

import cn.icylee.bean.Article;
import cn.icylee.bean.ArticleExample;
import cn.icylee.bean.UserExample;
import cn.icylee.dao.ArticleMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.front.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public List<Article> getArticleDraft(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        int uid = userMapper.selectByExample(userExample).get(0).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(uid).andStatusEqualTo("草稿");
        return articleMapper.selectByExample(articleExample);
    }

}
