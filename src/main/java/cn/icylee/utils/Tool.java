package cn.icylee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tool {

    public static String getOrderIdByUUId() {
        Calendar calendar = Calendar.getInstance();
        String number = calendar.get(Calendar.YEAR) + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + String.format("%2d", calendar.get(Calendar.DAY_OF_MONTH));
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
        return number + String.format("%010d", hashCodeV);
    }

    public static int setLevel(int grow) {
        int level = 0;
        if (grow >= 10000) {
            level = 10;
        } else if (grow >= 7000) {
            level = 9;
        } else if (grow >= 5000) {
            level = 8;
        } else if (grow >= 3000) {
            level = 7;
        } else if (grow >= 2000) {
            level = 6;
        } else if (grow >= 1000) {
            level = 5;
        } else if (grow >= 500) {
            level = 4;
        } else if (grow >= 200) {
            level = 3;
        } else if (grow >= 50) {
            level = 2;
        } else if (grow >= 1) {
            level = 1;
        }
        return level;
    }

    static class def {
        private static final int[] key = {1, 2, 3};
        private static final String[] value = {"最新发布", "最新回复", "最多回复"};
    }

    static class def_2 {
        private static final int[] key = {1, 2, 3, 4};
        private static final String[] value = {"最热", "最新发布", "最新回复", "最多回复"};
    }

    static class def_3 {
        private static final int[] key = {1, 2, 3};
        private static final String[] value = {"关注的人", "关注的板块", "关注的标签"};
    }

    static class tree {
        private static final int[] key = {1, 2, 3, 4, 5, 6, 7, 8};
        private static final String[] value = {"全部", "原创", "转载", "翻译", "问题求助", "行业动态", "分享", "解决方案", "改进意见"};
    }

    public static List<Object> tags(int defOrTree, int leftId) {
        List<Object> tags = new ArrayList<>();
        if (defOrTree == 1) {
            if (leftId == 1 || leftId == 3) {
                for (int i = 0; i < def.key.length; i++) {
                    Map<String, Object> tag = new HashMap<>();
                    tag.put("key", def.key[i]);
                    tag.put("value", def.value[i]);
                    tags.add(tag);
                }
            } else if (leftId == 2) {
                for (int i = 0; i < def_2.key.length; i++) {
                    Map<String, Object> tag = new HashMap<>();
                    tag.put("key", def_2.key[i]);
                    tag.put("value", def_2.value[i]);
                    tags.add(tag);
                }
            } else {
                for (int i = 0; i < def_3.key.length; i++) {
                    Map<String, Object> tag = new HashMap<>();
                    tag.put("key", def_3.key[i]);
                    tag.put("value", def_3.value[i]);
                    tags.add(tag);
                }
            }
        } else if (defOrTree == 2) {
            for (int i = 0; i < tree.key.length; i++) {
                Map<String, Object> tag = new HashMap<>();
                tag.put("key", tree.key[i]);
                tag.put("value", tree.value[i]);
                tags.add(tag);
            }
        }
        return tags;
    }

    private static final long HALF_DAY = 43200000L;

    private static final long ONE_DAY = 86400000L;

    private static final long ONE_WEEK = 604800000L;

    private static final long ONE_MONTH = 2592000000L;

    private static final long SIX_MONTH = 15552000000L;

    private static final long ONE_YEAR = 31536000000L;

    public static String time(int time) throws ParseException {
        long StartTime = 0L;

        if (time == 1) {
            StartTime = new Date().getTime() - HALF_DAY;
        } else if (time == 2) {
            StartTime = new Date().getTime() - ONE_DAY;
        } else if (time == 3) {
            StartTime = new Date().getTime() - ONE_WEEK;
        } else if (time == 4) {
            StartTime = new Date().getTime() - ONE_MONTH;
        } else if (time == 5) {
            StartTime = new Date().getTime() - SIX_MONTH;
        } else if (time == 6) {
            StartTime = new Date().getTime() - ONE_YEAR;
        }

        new Date().setTime(StartTime);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(StartTime));
    }

    private static String getFewDays(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static Date getFewDaysStart(int day) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getFewDays(day) + " 00:00:00");
    }

    public static Date getFewDaysFinal(int day) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getFewDays(day) + " 23:59:59");
    }

}
