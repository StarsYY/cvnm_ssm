package cn.icylee.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Usermedal {
    private Integer id;

    private Integer userid;

    private Integer medalid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    private String username;

    private String medal;

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getMedalid() {
        return medalid;
    }

    public void setMedalid(Integer medalid) {
        this.medalid = medalid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}