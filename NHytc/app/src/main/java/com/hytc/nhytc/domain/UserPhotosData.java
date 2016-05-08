package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/2/28.
 */
public class UserPhotosData extends BmobObject {
    private String userid;
    private String photourl;
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
