package com.hytc.nhytc.tool;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/4/27.
 */
public class Md5Util {

    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用于获取一个String的md5值
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }
}
