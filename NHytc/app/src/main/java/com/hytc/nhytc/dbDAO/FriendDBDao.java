package com.hytc.nhytc.dbDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hytc.nhytc.dbOpenHelper.DBOpenHelper;
import com.hytc.nhytc.domain.RYTokenUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 */
public class FriendDBDao {
    private Context context;
    private DBOpenHelper dbOpenHelper;
    public FriendDBDao(Context context){
        this.context = context;
        dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 增加一条数据
     */
    public void adddata(String token,String name,String head){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("token", token);
            values.put("name", name);
            values.put("head", head);
            db.insert("friend", null, values);
            db.close();
        }
    }


    /**
     * 删除一条记录
     */
    public void deletedata(String token){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("friend", "token=?", new String[]{token});
            db.close();
        }
    }

    /**
     * 修改一条数据
     */
    public void updatedata(String token,String name,String head){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("head", head);
            db.update("friend", values, "token=?", new String[]{token});
            db.close();
        }
    }

    /**
     * 查询某条记录是否存在
     */
    public boolean isExist(String token){
        boolean result = false;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("friend",null,"token=?",new String[]{token},null,null,null);
            if(cursor.moveToFirst()){
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 获取根据token获取姓名，头像路经
     */
    public RYTokenUser getUserInfo(String token){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("friend",null,"token=?",new String[]{token},null,null,null);
            if(cursor.moveToFirst()) {
                RYTokenUser user = new RYTokenUser();
                String newtoken = cursor.getString(cursor.getColumnIndex("token"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                user.setToken(newtoken);
                user.setName(name);
                user.setHead(head);
                cursor.close();
                db.close();
                return user;
            }
            cursor.close();
            db.close();
        }
        return null;
    }

    /**
     * 获取所有缓存的聊天好友信息
     * @return
     */
    public List<RYTokenUser> getAllData(){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        List<RYTokenUser> items = new ArrayList<>();
        if(db.isOpen()){
            Cursor cursor = db.query("friend",null,null,null,null,null,null);
            if(cursor.moveToNext()) {
                RYTokenUser item = new RYTokenUser();
                String newtoken = cursor.getString(cursor.getColumnIndex("token"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                item.setToken(newtoken);
                item.setName(name);
                item.setHead(head);
                items.add(item);
            }
            cursor.close();
            db.close();
        }
        return items;
    }
}
