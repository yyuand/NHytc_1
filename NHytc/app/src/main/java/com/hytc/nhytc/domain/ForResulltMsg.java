package com.hytc.nhytc.domain;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ForResulltMsg {
    //判断是否已赞
    private Boolean status;
    //记录说说在listVies中的位置
    private Integer position;
    private String commentcount;
    private String approvecount;

    public ForResulltMsg(Boolean status, Integer position, String commentcount, String approvecount) {
        this.status = status;
        this.position = position;
        this.commentcount = commentcount;
        this.approvecount = approvecount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getApprovecount() {
        return approvecount;
    }

    public void setApprovecount(String approvecount) {
        this.approvecount = approvecount;
    }
}
