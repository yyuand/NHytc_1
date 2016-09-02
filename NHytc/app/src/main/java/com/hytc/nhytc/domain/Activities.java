package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by dongf_000 on 2016/5/12 0012.
 */
public class Activities extends BmobObject {
    //活动名称
    private String name;
    //活动主题
    private String theme;
    //主题代号
    private int themeCode;
    //活动开始时间
    private String startTime;
    //活动结束时间
    private String endTime;
    //活动地点
    private String place;
    //主办单位
    private String holder;
    //活动内容
    private String content;
    //更多
    private String moreContent;
    //预览图片
    private String picURL;
    //联系方式
    private String connection;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    private User author;


    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    //图片名称
    private String picName;

    public String getConnection() { return connection; }

    public void setConnection(String connection) {  this.connection = connection; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getMoreContent() {
        return moreContent;
    }

    public void setMoreContent(String moreContent) {
        this.moreContent = moreContent;
    }

    public int getThemeCode() {
        return themeCode;
    }

    public void setThemeCode(int themeCode) {
        this.themeCode = themeCode;
    }
}
