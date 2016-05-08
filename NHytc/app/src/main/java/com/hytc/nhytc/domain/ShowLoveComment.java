package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/1/31.
 */
public class ShowLoveComment extends BmobObject {
    /**评论的内容*/
    private String content;
    /**评论在哪一条表白中*/
    private String showLoveId;
    /**是谁发起的评论*/
    private User author;
    /**评论的类型
     * true：评论的是表白，此时 commentedConment 的值为空
     * false：评论的是别人的评论，此时 commentedConment 里存的是被评论的评论
     * */
    private Boolean commentType;
    /**被评论的评论*/
    private ShowLoveComment commentedConment;

    public ShowLoveComment() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowLoveId() {
        return showLoveId;
    }

    public void setShowLoveId(String showLoveId) {
        this.showLoveId = showLoveId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Boolean getCommentType() {
        return commentType;
    }

    public void setCommentType(Boolean commentType) {
        this.commentType = commentType;
    }

    public ShowLoveComment getCommentedConment() {
        return commentedConment;
    }

    public void setCommentedConment(ShowLoveComment commentedConment) {
        this.commentedConment = commentedConment;
    }
}
