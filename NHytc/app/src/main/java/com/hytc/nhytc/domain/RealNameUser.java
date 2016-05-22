package com.hytc.nhytc.domain;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by DYY on 2016/5/14.
 */
public class RealNameUser extends BmobObject {
    /**申请人**/
    private User applicant;
    /**申请人姓名**/
    private String apply_Name;
    /**身份证号码**/
    private String iden_Num;
    /**身份证正面照**/
    private String first_Pic;
    /**身份证反面照**/
    private String sec_Pic;
    /**手持身份证照**/
    private String hand_Pic;
    /**图片的名字*/
    private List<String> picturesNames;

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getApply_Name() {
        return apply_Name;
    }

    public void setApply_Name(String apply_Name) {
        this.apply_Name = apply_Name;
    }

    public String getIden_Num() {
        return iden_Num;
    }

    public void setIden_Num(String iden_Num) {
        this.iden_Num = iden_Num;
    }

    public String getFirst_Pic() {
        return first_Pic;
    }

    public void setFirst_Pic(String first_Pic) {
        this.first_Pic = first_Pic;
    }

    public String getSec_Pic() {
        return sec_Pic;
    }

    public void setSec_Pic(String sec_Pic) {
        this.sec_Pic = sec_Pic;
    }

    public String getHand_Pic() {
        return hand_Pic;
    }

    public void setHand_Pic(String hand_Pic) {
        this.hand_Pic = hand_Pic;
    }

    public List<String> getPicturesNames() {
        return picturesNames;
    }

    public void setPicturesNames(List<String> picturesNames) {
        this.picturesNames = picturesNames;
    }
}
