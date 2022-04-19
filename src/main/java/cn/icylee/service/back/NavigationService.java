package cn.icylee.service.back;

import cn.icylee.bean.Navigation;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface NavigationService {

    int getNavigationTotal(TableParameter tableParameter);

    List<Navigation> getPageNavigation(TableParameter tableParameter);

    Navigation saveNavigation(Navigation navigation);

    int updateNavigation(Navigation navigation);

    int deleteNavigation(int id);
    
}
