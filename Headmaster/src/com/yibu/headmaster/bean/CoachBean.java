package com.yibu.headmaster.bean;

public class CoachBean {

	public Double latitude;
	public String name;
	public Boolean is_shuttle;
	public String coachid;
	public Double longitude;
	public Headportrait headportrait;
	public int starlevel;

	public class Headportrait {

		public String thumbnailpic;
		public String width;
		public String originalpic;
		public String height;
	}

	public Driveschoolinfo driveschoolinfo;

	public class Driveschoolinfo {

		public String name;
		public String id;
	}

}
