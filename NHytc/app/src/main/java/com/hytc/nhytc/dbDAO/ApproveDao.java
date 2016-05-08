package com.hytc.nhytc.dbDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hytc.nhytc.dbOpenHelper.DBOpenHelper;

/**
 * Created by Administrator on 2016/2/28.
 */
public class ApproveDao {
    private Context context;
    private DBOpenHelper dbOpenHelper;

    public ApproveDao(Context context) {
        this.context = context;
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 增加一条数据
     */
    public void adddata(String userid,String day){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("userid", userid);
            values.put("day", day);
            db.insert("approve", null, values);
            db.close();
        }
    }

    /**
     * 删除一条记录
     */
    public void deletedata(String userid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("approve", "userid=?", new String[]{userid});
            db.close();
        }
    }

    public void updataDay(String userid,String day){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("day", day);
            db.update("friend", values, "token=?", new String[]{userid});
            db.close();
        }
    }

    /**
     * 查询该条赞是否是在同一天
     * 这里的day的形式为 04 就是某月的4日
     *                 12 就是某月的12日
     */
    public boolean isOneDay(String userid,String day){
        boolean result = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("approve",null,"userid=?",new String[]{userid},null,null,null);
            if(cursor.moveToFirst()){
                String days = cursor.getString(cursor.getColumnIndex("day"));
                Log.e("approvedao",day +"   ===   "+ days);
                result = day.equals(days);
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 查询某条记录是否存在
     */
    public boolean isExist(String userid){
        boolean result = false;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("approve",null,"userid=?",new String[]{userid},null,null,null);
            if(cursor.moveToFirst()){
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }
}
