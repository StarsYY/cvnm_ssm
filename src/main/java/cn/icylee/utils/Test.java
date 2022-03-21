package cn.icylee.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Test {

    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%d", hashCodeV);
    }

    public static void main(String[] args) {
        String s = "12345678";
        System.out.println(s.substring(4, s.length()));

        Calendar calendar = Calendar.getInstance();
        String future = String.valueOf(calendar.get(Calendar.YEAR) + 5);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(future + time.substring(4));
    }

}
