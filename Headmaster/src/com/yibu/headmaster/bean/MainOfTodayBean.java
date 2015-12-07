package com.yibu.headmaster.bean;

import java.util.List;

public class MainOfTodayBean {

	public int applystudentcount; // 申请学生数量
	public int coursestudentnow; // 现在正在上课的学生数
	public int coachcoursenow; // 现在教练课时总数
	public int complaintstudentcount; // 投诉数量
	public int finishreservationnow; // 目前完成的课时数
	public int coachstotalcoursecount; // 总共课时数
	public int reservationcoursecountday; // 预约课时数
	public Commentstudentcount commentstudentcount;
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
