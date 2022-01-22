package cn.icylee.service.back;

import cn.icylee.bean.Root;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface RootService {

    int getRootTotal(TableParameter tableParameter);

    List<Root> getPageRoot(TableParameter tableParameter);

    Root saveRoot(Root root);

    Root updateRoot(Root root);

    int deleteRoot(int id);

}
