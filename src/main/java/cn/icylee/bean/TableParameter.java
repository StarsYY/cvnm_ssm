package cn.icylee.bean;

import lombok.Data;

@Data
public class TableParameter {
    private int page;

    private int limit;

    private String introduction;

    private String status;

    private String category;

    private String nickname;

    private String label;

    private String title;

    private String cids;

    private String ids;

    private String rootid;

    private String plateid;

    private String type;

    private String publish;

    private String tag;

    private String hot;

    private String plate;

    private String root;

    private String comment;

    private String article;

    private Integer id;

    private Integer userid;

    private String name;

    private Integer modularid;

    private String modular;

    private Integer transaction;

    private String payment;

    private String discuss;

    private String course;

    private String sort;

    public int getPage() {
        if (page != 0) {
            return (page - 1) * limit;
        } else {
            return 0;
        }
    }
}
