package com.hytc.nhytc.util;


import com.hytc.nhytc.domain.Lesson;

import java.util.ArrayList;
import java.util.List;


public class LessonDAO {

	public static List<Lesson> getLessonsByYearAndMonth(int year, int month) {
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lesson.setName("语文");
		lesson.setNum(1);
		lesson.setStatus(1);
		lesson.setLessonDate("2016-01-30 08:00");
		lesson.setsTime("08:00");
		lesson.seteTime("10:00");
		lesson.setWeek(1);
		lessons.add(lesson);
		
		Lesson lesson1 = new Lesson();
		lesson1.setId(2);
		lesson1.setName("数学");
		lesson1.setNum(2);
		lesson1.setStatus(0);
		lesson1.setLessonDate("2016-01-31 15:00");
		lesson1.setsTime("15:00");
		lesson1.seteTime("16:00");
		lesson1.setWeek(3);
		lessons.add(lesson1);
		
		Lesson lesson2 = new Lesson();
		lesson2.setId(2);
		lesson2.setName("英语");
		lesson2.setNum(3);
		lesson2.setStatus(1);
		lesson2.setLessonDate("2016-01-28 08:00");
		lesson2.setsTime("08:00");
		lesson2.seteTime("09:00");
		lesson2.setWeek(5);
		lessons.add(lesson2);
		return lessons;
	}

	public static List<Lesson> getLessonsByDate(String string, String nextDay) {
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lesson.setName("语文");
		lesson.setNum(1);
		lesson.setStatus(0);
		lesson.setLessonDate("2016-01-30 12:00");
		lesson.setsTime("08:00");
		lesson.seteTime("11:00");
		lesson.setStart_course(1);
		lesson.setEnd_course(3);
		lesson.setWeek(1);
		lessons.add(lesson);
		
		Lesson lesson1 = new Lesson();
		lesson1.setId(2);
		lesson1.setName("大学数学\n理工楼西阶101\n张伟");
		lesson1.setNum(2);
		lesson1.setStatus(0);
		lesson1.setLessonDate("2016-04-29 14:00");
		lesson1.setsTime("14:30");
		lesson1.seteTime("16:00");
		lesson1.setStart_course(4);
		lesson1.setEnd_course(5);
		lesson1.setWeek(2);
		lessons.add(lesson1);
		
		Lesson lesson2 = new Lesson();
		lesson2.setId(2);
		lesson2.setName("英语");
		lesson2.setNum(3);
		lesson2.setStatus(0);
		lesson2.setLessonDate("2016-01-28 08:00");
		lesson2.setsTime("08:00");
		lesson2.seteTime("09:00");
		lesson2.setStart_course(6);
		lesson2.setEnd_course(7);
		lesson2.setWeek(3);
		lessons.add(lesson2);

		Lesson lesson3 = new Lesson();
		lesson3.setId(2);
		lesson3.setName("C++程序设计@理工北楼419");
		lesson3.setNum(3);
		lesson3.setStatus(0);
		lesson3.setLessonDate("2016-01-28 08:00");
		lesson3.setsTime("08:00");
		lesson3.seteTime("12:25");
		lesson3.setStart_course(10);
		lesson3.setEnd_course(12);
		lesson3.setWeek(7);
		lessons.add(lesson3);
		return lessons;
	}
	
}
