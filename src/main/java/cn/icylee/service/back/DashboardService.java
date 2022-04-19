package cn.icylee.service.back;

import cn.icylee.bean.Order;

import java.text.ParseException;
import java.util.List;

public interface DashboardService {

    List<Order> getDashboardOrder();

    int getUserCount();

    int getArticleCount();

    String getMoney();

    int getCourseCount();

    int[] getUserChart() throws ParseException;

    int[] getArticleChart() throws ParseException;

    int[] getOrderChart() throws ParseException;

    int[] getCourseChart() throws ParseException;

}
