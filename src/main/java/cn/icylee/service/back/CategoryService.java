package cn.icylee.service.back;

import cn.icylee.bean.Category;
import cn.icylee.bean.Root;
import cn.icylee.bean.TableParameter;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    Map<String, Object> getAllRoot();

    int getCategoryTotal(TableParameter tableParameter);

    List<Category> getPageCategory(TableParameter tableParameter);

    Category saveCategory(Category category);

    Category updateCategory(Category category);

    int deleteCategory(int id);

}
