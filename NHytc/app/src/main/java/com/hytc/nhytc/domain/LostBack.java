package com.hytc.nhytc.domain;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/2/7.
 */
public class LostBack extends BmobObject {
    /**发布失物招领的人*/
    private User author;
    /**失物的类型*/
    private Integer lostType;
    /**功能（11:是失物招领   22:是寻物启事）*/
    private Integer lostfunction;
    /**失物的大概描述*/
    private String content;
    /**失物名称*/
    private String lostName;
    /**捡到失物的时间*/
    private String lostTime;
    /**捡到失物的地点*/
    private String lostLocation;
    /**失物的照片路径*/
    private List<String> pictures;
    /**图片的名字*/
    private List<String> picturesNames;

    public LostBack() {}

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getLostType() {
        return lostType;
    }

    public void setLostType(Integer lostType) {
        this.lostType = lostType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLostName() {
        return lostName;
    }

    public void setLostName(String lostName) {
        this.lostName = lostName;
    }

    public String getLostLocation() {
        return lostLocation;
    }

    public void setLostLocation(String lostLocation) {
        this.lostLocation = lostLocation;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getLostTime() {
        return lostTime;
    }

    public void setLostTime(String lostTime) {
        this.lostTime = lostTime;
    }

    public List<String> getPicturesNames() {
        return picturesNames;
    }

    public void setPicturesNames(List<String> picturesNames) {
        this.picturesNames = picturesNames;
    }

    public Integer getLostfunction() {
        return lostfunction;
    }

    public void setLostfunction(Integer lostfunction) {
        this.lostfunction = lostfunction;
    }
}
