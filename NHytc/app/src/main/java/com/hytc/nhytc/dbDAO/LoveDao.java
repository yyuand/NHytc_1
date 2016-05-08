package com.hytc.nhytc.dbDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hytc.nhytc.dbOpenHelper.DBOpenHelper;

/**
 * Created by Administrator on 2016/2/28.
 */
public class LoveDao {
    private Context context;
    private DBOpenHelper dbOpenHelper;

    public LoveDao(Context context) {
        this.context = context;
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 增加一条数据
     */
    public void adddata(String userid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("userid", userid);
            db.insert("love", null, values);
            db.close();
        }
    }

    /**
     * 删除一条记录
     */
    public void deletedata(String userid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("love", "userid=?", new String[]{userid});
            db.close();
        }
    }

    /**
     * 查询某条记录是否存在
     */
    public boolean isExist(String userid){
        boolean result = false;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("love",null,"userid=?",new String[]{userid},null,null,null);
            if(cursor.moveToFirst()){
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }
}
