package com.hytc.nhytc.tool;

/**
 * 这个工具类为什么会出现呢？那时因为一位同学对我的软件进行暴力测试
 * 就是快速地一直地点击某一个按钮，有可能会在短时间内向一个服务器接口进行大量
 * 的数据请求，从而出现问题。。。。。
 *
 * 通过这个类，我就可以控制让一个按钮在短时间内再次触发某个点击事件时无效
 *
 * Created by Administrator on 2016/3/11.
 */
public class ControlOnclick {

    private Long millis;

    public ControlOnclick(){
        this.millis = System.currentTimeMillis();
    }

    public Boolean isCanClick(Long milli){
        if((System.currentTimeMillis() - this.millis) > milli){
            this.millis = System.currentTimeMillis();
            return true;
        }else {
            this.millis = System.currentTimeMillis();
            return false;
        }
    }
}
