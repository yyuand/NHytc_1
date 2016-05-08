package com.hytc.nhytc.dbDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hytc.nhytc.dbOpenHelper.DBOpenHelper;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ApproveShuoDBDao {
    private Context context;
    private DBOpenHelper dbOpenHelper;

    public ApproveShuoDBDao(Context context) {
        this.context = context;
        this.dbOpenHelper = new DBOpenHelper(context);
    }
    /**
     * 增加一条数据
     */
    public void adddata(String shuoid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("shuoshuoid", shuoid);
            db.insert("approveshuo", null, values);
            db.close();
        }
    }

    /**
     * 删除一条记录
     */
    public void deletedata(String shuoid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("approveshuo", "shuoshuoid=?", new String[]{shuoid});
            db.close();
        }
    }

    /**
     * 查询某条记录是否存在
     */
    public boolean isExist(String shuoid){
        boolean result = false;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("approveshuo",null,"shuoshuoid=?",new String[]{shuoid},null,null,null);
            if(cursor.moveToFirst()){
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }


}
