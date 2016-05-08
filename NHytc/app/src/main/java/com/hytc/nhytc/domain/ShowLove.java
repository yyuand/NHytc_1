package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/1/31.
 */
public class ShowLove extends BmobObject {
    /**发起表白的人*/
    private User author;
    /**被表白人姓名*/
    private String showLoveName;
    /**表白的内容*/
    private String content;
    /**赞的数目*/
    private Integer approveCount;
    /**评论的数目*/
    private Integer commentCount;
    /**本人（看到该表白的人）是否赞过*/
    private Boolean isApprove;

    public ShowLove() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getShowLoveName() {
        return showLoveName;
    }

    public void setShowLoveName(String showLoveName) {
        this.showLoveName = showLoveName;
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
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }
}
