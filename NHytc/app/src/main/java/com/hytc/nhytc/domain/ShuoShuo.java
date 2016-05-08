package com.hytc.nhytc.domain;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ShuoShuo extends BmobObject {

    /**说说的主题*/
    private Integer topic;
    /**说说的内容*/
    private String content;
    /**说说被赞的数目*/
    private Integer approveCount;
    /****说说被评论的数目**/
    private Integer CommentCount;
    /****说说里连带的图片地址**/
    private List pictures;
    /**图片的名字*/
    private List<String> picturesNames;
    /****说说作者**/
    private User author;
    /****存储赞了该说说的人的objectid**/
    /**
     * 这个值与谁查该条信息有关，其默认值为false，在从服务器获取数据后才为其赋值
     * 比如我查看说说了，我看到了张三的说说，那么此时就会判断张三说说里的 approves
     * 里是否有本地即我的onjectid,如果有，此处为true，反之则为false
     *
     * 次值只在本地修改
     * **/
    private Boolean isApprove;


    public ShuoShuo() {
    }


    public Boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getApproveCount() {
        return approveCount;
    }

    public void setApproveCount(Integer approveCount) {
        this.approveCount = approveCount;
    }

    public Integer getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(Integer commentCount) {
        CommentCount = commentCount;
    }

    public List getPictures() {
        return pictures;
    }

    public void setPictures(List pictures) {
        this.pictures = pictures;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<String> getPicturesNames() {
        return picturesNames;
    }

    public void setPicturesNames(List<String> picturesNames) {
        this.picturesNames = picturesNames;
    }
}
