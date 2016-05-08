package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * 这张表主要存的就是回复消息
 * Created by Administrator on 2016/3/1.
 */
public class MyInfo extends BmobObject {
    /**给您回复的人*/
    private User author;
    /**回复的内容
     *
     * 如果type1 和 type2 是 {true    false} 或者是  {false   true}
     * 那么此处的content就存null
     *
     * */
    private String content;
    /**
     *该回复的类型，由type1和type2共同决定
     *true    true： 是说说中的回复
     *false   false：是表白中的回复
     *true    false：是有人赞了你
     *false   true： 是有人暗恋了你
     * */
    private Boolean type1;
    private Boolean type2;
    /**
     *该id存什么，由type1和type2共同决定
     *true    true： 是说说的id
     *false   false：是表白的id
     *true    false：是赞你的那个人的id，因为已经有了 author ，所以此时也可以为空
     *false   true： 是向你表白的那个人的id，因为已经有了 author ，所以此时也可以为空
     * */
    private String id;
    /**
     * 就是你本人的id,你就是通过这个id来查看你的消息的
     * */
    private String replyid;

    public MyInfo() {}

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getType1() {
        return type1;
    }

    public void setType1(Boolean type1) {
        this.type1 = type1;
    }

    public Boolean getType2() {
        return type2;
    }

    public void setType2(Boolean type2) {
        this.type2 = type2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }
}
