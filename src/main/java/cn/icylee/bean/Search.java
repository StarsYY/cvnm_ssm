package cn.icylee.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Search {
    private int page;

    private int limit;

    private String search;

    private int choose;

    private int plate;

    private int type;

    private int time;

    private String username;

    private Date startTime;

    private Date finalTime = new Date();

    public int getPage() {
        if (page != 0) {
            return (page - 1) * limit;
        } else {
            return 0;
        }
    }
}
