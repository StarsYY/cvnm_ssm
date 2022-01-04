package cn.icylee.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {

    private static final Map<String, Object> map = new HashMap<>();
    private static final Integer successCode = 20000;
    private static final Integer failCode = 50000;

    public static Map<String, Object> success(Object data, String message){
        map.put("code", successCode);
        map.put("data", data);
        map.put("message", message);
        return map;
    }

    public static Map<String, Object> error(String message){
        map.put("code", failCode);
        map.put("message", message);
        return map;
    }

}
