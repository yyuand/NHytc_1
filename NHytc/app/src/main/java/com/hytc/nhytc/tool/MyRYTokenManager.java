package com.hytc.nhytc.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/2/21.
 */
public class MyRYTokenManager {

    public static void saveToken(Context context,String rytoken){
        SharedPreferences sharedPreferences = context.getSharedPreferences("rytoken", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",rytoken);
        editor.apply();
    }

    public static String getRYToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("rytoken", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

}
