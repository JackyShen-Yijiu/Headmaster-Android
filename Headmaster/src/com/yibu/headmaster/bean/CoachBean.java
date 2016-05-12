package com.yibu.headmaster.bean;

import java.io.Serializable;



public class CoachBean implements Serializable{

	public Double latitude;
	public String name;
	public Boolean is_shuttle;
	public String coachid;
	public Double longitude;
	public Headportrait headportrait;
	public int starlevel;
	public int passrate;
	public String mobile;
	public Subject[]subject;
	public int isonline;
	public int coursecountr;
	
	

	public Driveschoolinfo driveschoolinfo;

	public class Driveschoolinfo implements Serializable{

		public String name;
		public String id;
	}

}
