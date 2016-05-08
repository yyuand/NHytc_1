package com.hytc.nhytc.dbOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/29.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "hytc.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS approveshuo (shuoshuoid varchar(15))");
        db.execSQL("CREATE TABLE IF NOT EXISTS approvelove (loveid varchar(15))");
        db.execSQL("CREATE TABLE IF NOT EXISTS love (userid varchar(15))");
        db.execSQL("CREATE TABLE IF NOT EXISTS approve (userid varchar(15),day varchar(15))");
        db.execSQL("CREATE TABLE IF NOT EXISTS friend (token varchar(100) primary key,name varchar(100),head varchar(100))");
        db.execSQL("CREATE TABLE IF NOT EXISTS course (courseid varchar(50) primary key,name varchar(200),week varchar(20),start_course varchar(20),end_course varchar(20),isdsh varchar(10),start_week varchar(20),end_week varchar(20),teacher varchar(50),address varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS course (courseid varchar(50) primary key,name varchar(200),week varchar(20),start_course varchar(20),end_course varchar(20),isdsh varchar(10),start_week varchar(20),end_week varchar(20),teacher varchar(50),address varchar(50))");
    }
}
