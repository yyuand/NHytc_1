package com.hytc.nhytc.domain;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Lesson  implements Serializable{
	private long id;				//课程id
	private String name;            //课程的主要内容
	private int num;				//第几课
	private String lessonDate;		//上课日期
	private String sTime;			//课程开始时间
	private String eTime;			//课程结束时间
	private int start_course;      //课是从第几节课开始的
	private int end_course;        //课是到第几节课结束的
	private int start_week;        //课程是从第几周开始的
	private int end_week;          //课程是从第几周结束的
	private int status;				//0 未上 1已上2 系统确认
	private int week;               //课程是在星期几
	private int isdsh;             //课程是否是单双周课 1：单周课   2：双周课    3：正常课
	private String teacher;        //上课老师
	private String adddress;        //上课地点
	private String courseid;        //课程的id(MD5构成)

	/**
	 *   此集合是用来存那些完整的课因为周数被拆分开来的情况
	 *
	 *
	 消费者行为研究
	 周一第10,11,12节{第12-16周}
	 欧阳芬
	 教B阶301

	 消费者行为研究
	 周一第10,11,12节{第1-2周}
	 欧阳芬
	 教B阶301

	 消费者行为研究
	 周一第10,11,12节{第4-10周}
	 欧阳芬
	 教B阶301
	 */
	private ArrayList<int[]> weeks;
	//如果为true，则weeks里面存有值
	private Boolean bo_we = false;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getLessonDate() {
		return lessonDate;
	}

	public void setLessonDate(String lessonDate) {
		this.lessonDate = lessonDate;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getLessonDateSTime(){
		return new StringBuffer().append(lessonDate).append(" ").append(this.sTime).toString();
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getStart_course() {
		return start_course;
	}

	public void setStart_course(int start_course) {
		this.start_course = start_course;
	}

	public int getEnd_course() {
		return end_course;
	}

	public void setEnd_course(int end_course) {
		this.end_course = end_course;
	}

	public int getIsdsh() {
		return isdsh;
	}

	public void setIsdsh(int isdsh) {
		this.isdsh = isdsh;
	}

	public int getEnd_week() {
		return end_week;
	}

	public void setEnd_week(int end_week) {
		this.end_week = end_week;
	}

	public int getStart_week() {
		return start_week;
	}

	public void setStart_week(int start_week) {
		this.start_week = start_week;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getAdddress() {
		return adddress;
	}

	public void setAdddress(String adddress) {
		this.adddress = adddress;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public ArrayList<int[]> getWeeks() {
		return weeks;
	}

	public void setWeeks(ArrayList<int[]> weeks) {
		this.weeks = weeks;
	}

	public Boolean getBo_we() {
		return bo_we;
	}

	public void setBo_we(Boolean bo_we) {
		this.bo_we = bo_we;
	}
}
