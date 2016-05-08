package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Subscribe extends BmobObject {
    /**订阅的名字*/
    private String sub_name;
    /**订阅的介绍*/
    private String sub_introduce;
    /**该订阅是否显示*/
    private Boolean is_show;
    /**该订阅的标号
     * 用于查看详情，对号入座
     * */
    private Integer sub_number;
    /**订阅图片的url*/
    private String pic_path;

    private Integer sort;

    public Subscribe() {}

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_introduce() {
        return sub_introduce;
    }

    public void setSub_introduce(String sub_introduce) {
        this.sub_introduce = sub_introduce;
    }

    public Boolean getIs_show() {
        return is_show;
    }

    public void setIs_show(Boolean is_show) {
        this.is_show = is_show;
    }

    public Integer getSub_number() {
        return sub_number;
    }

    public void setSub_number(Integer sub_number) {
        this.sub_number = sub_number;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
