package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/1/25.
 */
public class User extends BmobUser {
    /**学号*/
    private String stu_number;
    /**学院*/
    private String faculty;
    /**性别*/
    private Boolean gender;
    /**个性签名*/
    private String autograhp;
    /**头像路径*/
    private String headSculpture;
    /**头像上传成功的文件名*/
    private String headerfilename;
    /**被赞个数*/
    private Integer praise;
    /**被暗恋的个数*/
    private Integer loved;
    /**是否实名认证**/
    private Boolean isRealName;

    public Boolean getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(Boolean isRealName) {
        this.isRealName = isRealName;
    }



    public User() {}

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }

    public String getStu_number() {
        return stu_number;
    }


    public void setStu_number(String stu_number) {
        this.stu_number = stu_number;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getAutograhp() {
        return autograhp;
    }

    public void setAutograhp(String autograhp) {
        this.autograhp = autograhp;
    }

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }

    public Integer getLoved() {
        return loved;
    }

    public void setLoved(Integer loved) {
        this.loved = loved;
    }

    public String getHeaderfilename() {
        return headerfilename;
    }

    public void setHeaderfilename(String headerfilename) {
        this.headerfilename = headerfilename;
    }
}
