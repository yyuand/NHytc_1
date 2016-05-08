package com.hytc.nhytc.domain;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/2/9.
 */
public class Merchandise extends BmobObject{
    /**发布商品的人*/
    private User author;
    /**商品类型*/
    private Integer type;
    /**商品名*/
    private String MerchandiseName;
    /**想卖的价格*/
    private String price;
    /**商品的原价*/
    private String oldPrice;
    /**商品的大概描述*/
    private String content;
    /**商品的照片*/
    private List<String> pictures;
    /**图片的名字*/
    private List<String> picturesNames;

    public Merchandise() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMerchandiseName() {
        return MerchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        MerchandiseName = merchandiseName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getPicturesNames() {
        return picturesNames;
    }

    public void setPicturesNames(List<String> picturesNames) {
        this.picturesNames = picturesNames;
    }
}
