package com.yibu.headmaster.bean;

import java.io.Serializable;

public class Studentinfo implements Serializable{
	public Classtype classtype;
	public Headportrait headportrait;
	public String mobile;
	public String name;
	public String userid;
	
	public class Headportrait implements Serializable{

		public String thumbnailpic;
		public String width;
		public String originalpic;
		public String height;
	}
	public class Classtype implements Serializable{

		public String id;
		public String name;
		public int price;
	}
}
