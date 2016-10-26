package com.jzjf.headmaster.bean;

import java.util.List;

public class MoreDataOfWeekBean {

	public List<Datalist> datalist;

	public class Datalist {

		public int applystudentcount;
		public int generalcomment;
		public int goodcommentcount;
		public int complaintcount;
		public String summarytime;
		public int badcommentcount;
		public int day; // 周几
		public int reservationcoursecount;
		public int weekindex; // 第几周
		public int month; // 第几月
	}

	public List<Coursedata> coursedata;

	public class Coursedata {

		public int coursecount;
		public int coachcount;
	}

}