package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeDetail extends BmobObject {
    /**单条新闻消息的名称*/
    private String name;
    /**新闻的简介*/
    private String introduce;
    /**如果是广告，交了多少钱*/
    private Float money;
    /**新闻是属于哪一种订阅*/
    private Subscribe type;
    /**是否显示该新闻*/
    private Boolean is_show;
    /**新闻配图的url*/
    private String picture_url;
    /**新闻对应的网页的url*/
    private String web_url;

    private Integer sort;

    public SubscribeDetail() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Subscribe getType() {
        return type;
    }

    public void setType(Subscribe type) {
        this.type = type;
    }

    public Boolean getIs_show() {
        return is_show;
    }

    public void setIs_show(Boolean is_show) {
        this.is_show = is_show;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
