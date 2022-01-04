package cn.icylee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tool {

    public static String setLevel(String Grow) {
        String level = "0";
        int grow = Integer.parseInt(Grow);
        if (grow >= 10000) {
            level = "10";
        } else if (grow >= 7000) {
            level = "9";
        } else if (grow >= 5000) {
            level = "8";
        } else if (grow >= 3000) {
            level = "7";
        } else if (grow >= 2000) {
            level = "6";
        } else if (grow >= 1000) {
            level = "5";
        } else if (grow >= 500) {
            level = "4";
        } else if (grow >= 200) {
            level = "3";
        } else if (grow >= 50) {
            level = "2";
        } else if (grow >= 1) {
            level = "1";
        }
        return level;
    }

    static class def {
        private static final int[] key = {1, 2, 3};
        private static final String[] value = {"最新发布", "最新回复", "最多回复"};
        private static final int[] tagIndex = {1, 0, 0};
    }

    static class tree {
        private static final int[] key = {1, 2, 3, 4};
        private static final String[] value = {"t", "r", "e", "e"};
        private static final int[] tagIndex = {1, 0, 0, 0};
    }

    public static List<Object> tags(int defOrTree) {
        List<Object> tags = new ArrayList<>();
        if (defOrTree == 1) {
            for (int i = 0; i < def.key.length; i++) {
                Map<String, Object> tag = new HashMap<>();
                tag.put("key", def.key[i]);
                tag.put("value", def.value[i]);
                tag.put("tagIndex", def.tagIndex[i]);
                tags.add(tag);
            }
        } else if (defOrTree == 2) {
            for (int i = 0; i < tree.key.length; i++) {
                Map<String, Object> tag = new HashMap<>();
                tag.put("key", tree.key[i]);
                tag.put("value", tree.value[i]);
                tag.put("tagIndex", tree.tagIndex[i]);
                tags.add(tag);
            }
        }
        return tags;
    }

}
