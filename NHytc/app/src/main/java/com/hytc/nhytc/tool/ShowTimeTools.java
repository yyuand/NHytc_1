package com.hytc.nhytc.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ShowTimeTools {
    public static String getShowTime(String time){
        String resulttime = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);

        /**
         * 昨天
         */
        Date zuoDate = new Date(System.currentTimeMillis() - 86400000);
        String zuostr = formatter.format(zuoDate);

        /**
         * 前天
         */
        Date qianDate = new Date(System.currentTimeMillis() - 172800000);
        String qianstr = formatter.format(qianDate);

        if(time.substring(0,10).equals(str.substring(0,10))){
            resulttime = "今天" + time.substring(11,16);
        }else if(time.substring(0,10).equals(zuostr.substring(0,10))){
            resulttime = "昨天" + time.substring(11,16);
        }else if(time.substring(0,10).equals(qianstr.substring(0,10))){
            resulttime = "前天" + time.substring(11,16);
        }else if(time.substring(0,4).equals(str.substring(0,4))){
            resulttime = time.substring(5,16);
        }else {
            resulttime = time;
        }

        return resulttime;
    }
}
