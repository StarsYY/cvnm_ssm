package cn.icylee.utils;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%d", hashCodeV);
    }

    public static void main(String[] args) throws ParseException {
        /*String s = "12345678";
        System.out.println(s.substring(4, s.length()));

        Calendar calendar = Calendar.getInstance();
        String future = String.valueOf(calendar.get(Calendar.YEAR) + 5);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list = list.subList(0, 3);
        Collections.shuffle(list);
//        System.out.println(list);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String s1 = year + "-" + month + "-" + day + " 00:00:00";
        String s2 = year + "-" + month + "-" + day + " 23:59:59";
        System.out.println(s2);
        System.out.println("****************");


        System.out.println(new Date());


        String string = "2022-03-21 20:20:20";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(string);
        System.out.println(date);


        Calendar ca = Calendar.getInstance();
        System.out.println(ca.getActualMaximum(Calendar.DAY_OF_MONTH));*/


//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH ) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
//        String START = year + "-" + month + "-" + day + " 00:00:00";
//        String FINAL = year + "-" + month + "-" + day + " 23:59:59";
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        Date dateStart = format.parse(START);
//        Date dateFinal = format.parse(FINAL);
//
//
//        long time = new Date().getTime() - ONE_DAY * 2;
//        new Date().setTime(time);
//        System.out.println(time);
//
//        System.out.println("格式化结果：" + new Date(time));

//        Calendar now = Calendar.getInstance();
//        now.add(Calendar.DAY_OF_MONTH, -0);
//        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
//        endDate += " 00:00:00";
//        Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(endDate);
//        System.out.println(dateStart);


//        System.out.println(new Random().nextInt(2) + 1);

//        System.out.println(new Date().getTime());
    }

    private static final long HALF_DAY = 43200000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;
    private static final long ONE_MONTH = 2592000000L;
    private static final long SIX_MONTH = 2592000000L;
    private static final long ONE_YEAR = 31536000000L;

}
