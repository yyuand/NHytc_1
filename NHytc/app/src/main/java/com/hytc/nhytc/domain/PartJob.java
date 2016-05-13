package com.hytc.nhytc.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by DYY on 2016/5/10.
 */
public class PartJob extends BmobObject {
	/**兼职的名称**/
	private String job_Name;
	/**发布兼职的老板**/
	private User boss;
	/**兼职的时间**/
	private String time;
	/**兼职的地点**/
	private String place;
	/**兼职的要求**/
	private String require;
	/**兼职的薪水**/
	private String salary;
	/**兼职的具体描述**/
	private String describe;
	/**老板的联系方式**/
	private String tel;

	public User getBoss() {
		return boss;
	}

	public void setBoss(User boss) {
		this.boss = boss;
	}

	public String getJob_Name() {
		return job_Name;
	}

	public void setJob_Name(String job_Name) {
		this.job_Name = job_Name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRequire() {
		return require;
	}

	public void setRequire(String require) {
		this.require = require;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
