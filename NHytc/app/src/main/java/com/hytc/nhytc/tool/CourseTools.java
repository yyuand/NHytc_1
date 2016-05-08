package com.hytc.nhytc.tool;

import android.content.Context;

import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/26.
 */
public class CourseTools {

    /**
     * 将该课程上的第几节课给提取出来
     * @param data 课程信息
     * @return 该课程的上课时间信息
     */
    private static List<String> getCourses(String data) {
        Pattern patt = Pattern.compile("\\d+[,节]");
        Matcher mmaa = patt.matcher(data);
        List<String> items = new ArrayList<>();
        while(mmaa.find()){
            String s = mmaa.group().toString();
            items.add(s.substring(0,s.length() - 1));
        }
        return items;
    }



    /**
     * 将该课程上的上课周数给提取出来
     * @param data 课程信息
     * @return 该课程的上课周数信息
     */
    private static List<String> getWeek(String data) {
        Pattern patt = Pattern.compile("\\d+[-周]");
        Matcher mmaa = patt.matcher(data);
        List<String> items = new ArrayList<>();
        while(mmaa.find()){
            String s = mmaa.group().toString();
            items.add(s.substring(0,s.length() - 1));
        }
        return items;
    }

    /**
     * 该课程是单周的课程还是双周的课程
     * @param data 课程信息
     * @return 1：单周，2：双周，3：正常的课程，不存在单双周
     */
    private static int isDSH(String data) {
        if(data.matches(".*单.*")){
            return 1;
        }else if(data.matches(".*双.*")){
            return 2;
        }else{
            return 3;
        }
    }


    /**
     *   算法设计与分析
         五
         3------------->4
         1------------->15
         单周课
         张巍
         理工楼西阶101
     * @param items
     * @return
     */
    private static Lesson doWithCourse4(List<String> items){
        Lesson lesson = new Lesson();

        String md5 = Md5Util.getMd5(String.valueOf(System.currentTimeMillis()));
        lesson.setCourseid(md5);

        lesson.setName(items.get(0));

        switch (items.get(1).substring(1,2)){
            case "一":
                lesson.setWeek(1);
                break;
            case "二":
                lesson.setWeek(2);
                break;
            case "三":
                lesson.setWeek(3);
                break;
            case "四":
                lesson.setWeek(4);
                break;
            case "五":
                lesson.setWeek(5);
                break;
            case "六":
                lesson.setWeek(6);
                break;
            case "日":
                lesson.setWeek(7);
                break;
        }

        List<String> courses = getCourses(items.get(1));
        lesson.setStart_course(Integer.parseInt(courses.get(0)));
        lesson.setEnd_course(Integer.parseInt(courses.get(courses.size() - 1)));


        List<String> weeks = getWeek(items.get(1));
        lesson.setStart_week(Integer.parseInt(weeks.get(0)));
        lesson.setEnd_week(Integer.parseInt(weeks.get(weeks.size() - 1)));

        lesson.setIsdsh(isDSH(items.get(1)));

        lesson.setTeacher(items.get(2));

        lesson.setAdddress(items.get(3));

        return lesson;
    }


    public static List<Lesson> doWithCourse(List<String> items){
        List<Lesson> lessons = new ArrayList<>();
        for(int i = 0;i < items.size(); i ++){
            /**
             * 以周开头，并且包含” { “符号
             */
            if("周".equals(items.get(i).substring(0,1)) && items.get(i).matches(".*\\{.*")){
                List<String> datas = new ArrayList<>();
                datas = items.subList(i - 1, i + 3);
                lessons.add(doWithCourse4(datas));
            }
        }
        return lessons;
    }

    public static int getCurrentWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl = Calendar.getInstance();
        try {
            cl.setTime(sdf.parse(sdf.format(new Date())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int week = cl.get(Calendar.WEEK_OF_YEAR);
        return week;
        //System.out.println(week);
        /*cl.add(Calendar.DAY_OF_MONTH, -7);
        int year = cl.get(Calendar.YEAR);
        if(week<cl.get(Calendar.WEEK_OF_YEAR)){
            year+=1;
        }
        System.out.println(year+"年第"+week+"周");*/
    }
}
