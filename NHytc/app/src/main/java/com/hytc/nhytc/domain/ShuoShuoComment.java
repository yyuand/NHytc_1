package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ShuoShuoComment extends BmobObject {

    /****评论的内容**/
    private String content;

    /****评论在哪一条说说中**/
    private String shuoshuoid;

    /****是谁发起的评论**/
    private User commentUser;

    /**评论的类型
     * true：评论的是说说，此时 commentedConment 的值为空
     * false：评论的是别人的评论，此时 commentedConment 里存的是被评论的评论
     * */
    private Boolean commentType;

    /**被评论的评论*/
    private ShuoShuoComment commentedConment;


    public ShuoShuoComment() {
    }

    public ShuoShuoComment getCommentedConment() {
        return commentedConment;
    }

    public void setCommentedConment(ShuoShuoComment commentedConment) {
        this.commentedConment = commentedConment;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShuoshuoid() {
        return shuoshuoid;
    }

    public void setShuoshuo(String shuoshuoid) {
        this.shuoshuoid = shuoshuoid;
    }

    public User getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }

    public Boolean getCommentType() {
        return commentType;
    }

    public void setCommentType(Boolean commentType) {
        this.commentType = commentType;
    }

}
