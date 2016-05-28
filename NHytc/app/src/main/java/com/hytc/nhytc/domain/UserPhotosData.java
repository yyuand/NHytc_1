package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * 用户的照片数据
 * Created by Administrator on 2016/2/28.
 */
public class UserPhotosData extends BmobObject {
    //用户的id
    private String userid;
    //用户相册的照片
    private String photourl;
    //用于Bmob在服务器上的操作
    private String filename;

    public UserPhotosData() {}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
