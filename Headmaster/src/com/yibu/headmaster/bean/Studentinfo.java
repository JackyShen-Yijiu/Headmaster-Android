package com.yibu.headmaster.bean;

public class Studentinfo {
	public Classtype classtype;
	public Headportrait headportrait;
	public String mobile;
	public String name;
	public String userid;
	
	public class Headportrait {

		public String thumbnailpic;
		public String width;
		public String originalpic;
		public String height;
	}
	public class Classtype {

		public String id;
		public String name;
		public int price;
	}
}
