package com.yibu.headmaster.bean;

import java.util.List;

public class MainPageDataV2Bean {

	public int applystudentcount; // 申请学生数量
	public Commentstudentcount commentstudentcount;
	public int complaintstudentcount; // 投诉数量
	public int[] overstockstudent;//积压人数
	public int[] passrate; //通过率
	public List<Schoolstudentcount> schoolstudentcount;

	public class Commentstudentcount {

		public int generalcomment;
		public int goodcommnent; // 好评数量
		public int badcomment;
	}

	public class Schoolstudentcount {
		public int subjectid; // 科目id
		public int studentcount; // 人员数量
	}

}
