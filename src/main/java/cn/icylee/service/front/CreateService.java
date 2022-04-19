package cn.icylee.service.front;

import cn.icylee.bean.Article;

import java.util.List;

public interface CreateService {

    List<Article> getArticleDraft(String username);

}
