package cn.icylee.service.back;

import cn.icylee.bean.Order;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface OrderService {

    TableParameter setSelectTool(TableParameter tableParameter);

    int getOrderTotal(TableParameter tableParameter);

    List<Order> getPageOrder(TableParameter tableParameter);

    int deleteOrder(int id);

}
