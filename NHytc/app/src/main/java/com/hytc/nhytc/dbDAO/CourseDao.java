package com.hytc.nhytc.dbDAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hytc.nhytc.dbOpenHelper.DBOpenHelper;
import com.hytc.nhytc.domain.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class CourseDao {
    private Context context;
    private DBOpenHelper dbOpenHelper;
    private SharedPreferences sp;

    public CourseDao(Context context) {
        this.context = context;
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 增加数据
     */
    public void addData(List<Lesson> items){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(db.isOpen()){
            for(Lesson lesson : items){
                values.clear();
                values.put("courseid", lesson.getCourseid());
                values.put("name",lesson.getName());
                values.put("week",lesson.getWeek());
                values.put("start_course",lesson.getStart_course());
                values.put("end_course",lesson.getEnd_course());
                values.put("isdsh",lesson.getIsdsh());
                values.put("start_week",lesson.getStart_week());
                values.put("end_week",lesson.getEnd_week());
                values.put("teacher",lesson.getTeacher());
                values.put("address",lesson.getAdddress());
                db.insert("course",null,values);
            }
            db.close();
        }
    }


    /**
     * 获取数据库中的所有课表信息
     * @return 课程数据的集合
     */
    public List<Lesson> getCourseData(){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        List<Lesson> items = new ArrayList<>();
        if(db.isOpen()){
            Cursor cursor = db.query("course",null,null,null,null,null,null);
            while(cursor.moveToNext()){
                Lesson item = new Lesson();
                item.setId(1);
                item.setNum(1);
                item.setLessonDate("2016-04-29 14:00");
                item.setsTime("8:00");
                item.seteTime("10:00");
                item.setStatus(0);

                //以上的5个成员变量用不到，所以就随便设置个默认值

                item.setCourseid(cursor.getString(cursor.getColumnIndex("courseid")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setWeek(Integer.parseInt(cursor.getString(cursor.getColumnIndex("week"))));
                item.setStart_course(Integer.parseInt(cursor.getString(cursor.getColumnIndex("start_course"))));
                item.setEnd_course(Integer.parseInt(cursor.getString(cursor.getColumnIndex("end_course"))));
                item.setIsdsh(Integer.parseInt(cursor.getString(cursor.getColumnIndex("isdsh"))));
                item.setStart_week(Integer.parseInt(cursor.getString(cursor.getColumnIndex("start_week"))));
                item.setEnd_week(Integer.parseInt(cursor.getString(cursor.getColumnIndex("end_week"))));
                item.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                item.setAdddress(cursor.getString(cursor.getColumnIndex("address")));

                items.add(item);

            }
            cursor.close();
            db.close();
        }
        Log.e("coursedao",items.size() + "");
        return items;
    }


    /**
     * 看看数据库中是否存在课程数据
     * @return true 存在   false 不存在
     */
    public boolean isExistData(){
        boolean isexist = false;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.query("course",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                isexist = true;
            }
            cursor.close();
            db.close();
        }
        return isexist;
    }


    /**
     * 删除数据库中的所有数据
     */
    public void deleteAllData(){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("course", null, null);
        }
        db.close();
    }

    public Lesson getlesson(String courseid){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            Lesson item = new Lesson();
            Cursor cursor = db.query("course",null,"courseid=?",new String[]{courseid},null,null,null);
            if(cursor.moveToFirst()){
                item.setId(1);
                item.setNum(1);
                item.setLessonDate("2016-04-29 14:00");
                item.setsTime("8:00");
                item.seteTime("10:00");
                item.setStatus(0);

                //以上的5个成员变量用不到，所以就随便设置个默认值

                item.setCourseid(cursor.getString(cursor.getColumnIndex("courseid")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setWeek(Integer.parseInt(cursor.getString(cursor.getColumnIndex("week"))));
                item.setStart_course(Integer.parseInt(cursor.getString(cursor.getColumnIndex("start_course"))));
                item.setEnd_course(Integer.parseInt(cursor.getString(cursor.getColumnIndex("end_course"))));
                item.setIsdsh(Integer.parseInt(cursor.getString(cursor.getColumnIndex("isdsh"))));
                item.setStart_week(Integer.parseInt(cursor.getString(cursor.getColumnIndex("start_week"))));
                item.setEnd_week(Integer.parseInt(cursor.getString(cursor.getColumnIndex("end_week"))));
                item.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                item.setAdddress(cursor.getString(cursor.getColumnIndex("address")));
            }
            cursor.close();
            db.close();
            return item;
        }
        return null;
    }


    public void deleteSpData(){
        sp = context.getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("stunum","");
        editor.putString("stupwd","");
        editor.putBoolean("issave", false);
        editor.apply();
    }


}
