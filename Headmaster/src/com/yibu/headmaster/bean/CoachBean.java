package com.yibu.headmaster.bean;

import android.R.integer;

import com.yibu.headmaster.bean.AssessBean.Commentlist.Subject;

public class CoachBean {

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
	
	public class Subject {

		public String _id;
		public String name;
		public int subjectid;
	}

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
