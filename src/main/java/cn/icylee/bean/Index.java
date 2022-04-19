package cn.icylee.bean;

import lombok.Data;

@Data
public class Index {
    private Integer defOrTree;

    private Integer leftId;

    private Integer contentTag;

    private Integer uid;

    private String username;

    private String loginName;

    private String title;

    private String type;

    private String hot;

    private Integer payment;

    private String ids;

    private int page;

    private int limit;

    public int getPage() {
        return page * limit;
    }
}
