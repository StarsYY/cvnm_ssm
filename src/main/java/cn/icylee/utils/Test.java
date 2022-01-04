package cn.icylee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Object> list1 = new ArrayList<>();
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.addAll(list);
        System.out.println(list1);
    }

}
