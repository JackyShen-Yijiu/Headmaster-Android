package com.yibu.headmaster.bean;

import java.util.List;

public class MoreDataBean {

	public List<Applystuentlist> applystuentlist;

	public class Applystuentlist {

		public int applystudentcount;
		public int hour;
	}

	public List<Goodcommentlist> goodcommentlist;

	public class Goodcommentlist {

		public int hour;
		public int commnetcount;
	}

	public List<Coachcourselist> coachcourselist;

	public class Coachcourselist {

		public int coursecount;
		public int coachcount;
	}

	public List<Complaintlist> complaintlist;

	public class Complaintlist {

		public int hour;
		public int complaintcount;
	}

	public List<Badcommentlist> badcommentlist;

	public class Badcommentlist {

		public int hour;
		public int commnetcount;
	}

	public List<Generalcommentlist> generalcommentlist;

	public class Generalcommentlist {

		public int hour;
		public int commnetcount;
	}

	public List<Reservationstudentlist> reservationstudentlist;

	public class Reservationstudentlist {

		public int studentcount;
		public int hour;
	}

}