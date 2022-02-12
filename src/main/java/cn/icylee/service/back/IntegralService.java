package cn.icylee.service.back;

import cn.icylee.bean.Integral;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface IntegralService {

    TableParameter setIdsTool(TableParameter tableParameter);

    int getIntegralTotal(TableParameter tableParameter);

    List<Integral> getPageIntegral(TableParameter tableParameter);

    int deleteIntegral(int id);

}
