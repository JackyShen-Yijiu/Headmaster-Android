package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.AssessAdapter;
import com.yibu.headmaster.adapter.ComplaintAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.bean.AssessBean;
import com.yibu.headmaster.bean.AssessBean.Commentcount;
import com.yibu.headmaster.bean.AssessBean.Commentlist;
import com.yibu.headmaster.bean.ComplaintBean;
import com.yibu.headmaster.datachart.PieChart01View;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class AssessDetailPager extends BasePagerFragment implements OnClickListener{

	private View view;
	private ArrayList<Commentlist> listAssess = new ArrayList<Commentlist>();
	private ArrayList<ComplaintBean> listComplaint = new ArrayList<ComplaintBean>();
	private AssessAdapter adapterAssess;
	private ComplaintAdapter adapterComplaint;
	private int curpage = 1;
	private int commentlevel = 3; // 评价等级 1 差评2 中评 3 好评
	private View viewHeader;
	// 刷新
	private ProgressBar progressBar_main;
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_assess;
	private RelativeLayout relativeLayout_ring;

	private PieChart01View assessThan;// 评价比列

	private int searchtype = 1; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年 6： 上周 7 上月
	private boolean hasMoreData = true;
	private TextView tv_assess_number;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;

	public AssessDetailPager(Context context, int i, int searchtype) {
		super(context);
		searchtype = i;
		 switch (i) {
		 case 1:
			  this.searchtype = 7;
			 break;
		 case 2:
			  this.searchtype = 6;
			 break;
		 case 3:
			  this.searchtype = 1;
			 break;
		 case 4:
			  this.searchtype = 3;
			 break;
		 case 5:
			  this.searchtype = 4;
			 break;
		 default:
			 this.searchtype = 1;
		 break;
		 }
		this.searchtype = searchtype;
	}

	@Override
	public View initView() {
		view = View.inflate(mContext, R.layout.assess_main, null);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.assess_pullToRefreshListView);
		pullToRefreshListView.setMode(Mode.BOTH);
		progressBar_main = (ProgressBar) view
				.findViewById(R.id.assess_progressBar_main);
		list_assess = pullToRefreshListView.getRefreshableView();
		list_assess.setCacheColorHint(Color.TRANSPARENT);
		list_assess.setDividerHeight(0);
		list_assess.setSelector(R.drawable.listview_selector);

		viewHeader = View.inflate(mContext, R.layout.assess_head_view, null);
		relativeLayout_ring = (RelativeLayout) viewHeader
				.findViewById(R.id.RelativeLayout_ring);
		tv_assess_number=(TextView)viewHeader.findViewById(R.id.tv_assess_number);
		textView1=(TextView)viewHeader.findViewById(R.id.textView1);
		textView2=(TextView)viewHeader.findViewById(R.id.textView2);
		textView3=(TextView)viewHeader.findViewById(R.id.textView3);
		list_assess.addHeaderView(viewHeader);

		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void initData() {
//		if (commentlevel == 4) {
//			adapterComplaint = new ComplaintAdapter(mContext, listComplaint);
//			list_assess.setAdapter(adapterComplaint);
//		} else {

			adapterAssess = new AssessAdapter(mContext, listAssess);
			list_assess.setAdapter(adapterAssess);
//		}
//		if (commentlevel == 4) {
//			list_assess.removeHeaderView(viewHeader);
//		} else {
//			// list_assess.removeHeaderView(viewHeader);
//
//		}

		// 给PullToRefreshListView设置监听器
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							curpage = 1;
							loadNetworkData();
						} else {
							if (!hasMoreData) {
								// ToastUtil.showToast(mContext, "没有更多数据了");
								// // hasMoreData = false;
							} else {
								curpage++;
							}
							loadNetworkData();
						}
					}
				});
		loadNetworkData();
	}

	private void loadNetworkData() {

		if (!hasMoreData) {
			ToastUtil.showToast(mContext, "没有更多数据了");
			pullToRefreshListView.postDelayed(new Runnable() {

				@Override
				public void run() {
					pullToRefreshListView.onRefreshComplete();
				}
			}, 100);
		}
//		if (commentlevel == 4) {
//
//			ApiHttpClient.get("statistics/complaintdetails?userid="
//					+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
//					+ HeadmasterApplication.app.userInfo.driveschool.schoolid
//					+ "&index=" + curpage + "&count=10", handler);
//		} else {

			LogUtil.print("评论详情：加载第几页：" + curpage);
			ApiHttpClient.get("statistics/commentdetails?userid="
					+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
					+ HeadmasterApplication.app.userInfo.driveschool.schoolid
					+ "&index=" + curpage + "&count=10&searchtype="
					+ searchtype + "&commentlevel=" + commentlevel, handler);
//		}
	}

	@Override
	public void process(String data) {
		// 加载
//		if (commentlevel == 4) {
//
//			List<ComplaintBean> comList = (List<ComplaintBean>) JsonUtil
//					.parseJsonToList(data,
//							new TypeToken<List<ComplaintBean>>() {
//							}.getType());
//			if (comList != null) {
//
//				if (curpage == 1) {
//					listComplaint.clear();
//				}
//				if (comList.size() == 0 && curpage != 1) {
//					hasMoreData = false;
//
//				} else {
//					listComplaint.addAll(comList);
//					adapterComplaint.notifyDataSetChanged();
//					progressBar_main.setVisibility(View.GONE);
//
//				}
//			}
//		} else {
			AssessBean assessBean = JsonUtil.parseJsonToBean(data,
					AssessBean.class);
			List<Commentlist> commentlist = null;

			if (assessBean != null) {
				commentlist = assessBean.commentlist;
			}
			if (curpage == 1) {
				listAssess.clear();
			}
			if (commentlist.size() == 0) {
				if (curpage == 1) {
					// if(v)
					// System.out.println("没有数据了。。。。。。");
					// ToastUtil.showToast(mContext, "没有数据");
				} else {
					hasMoreData = false;

				}
			} else {
				listAssess.addAll(commentlist);
				adapterAssess.notifyDataSetChanged();

			}
			// 评价比列--------------------------
			if (assessBean != null) {
				Commentcount commentcount = assessBean.commentcount;
				if (relativeLayout_ring.getChildCount()==0) {
					assessThan = new PieChart01View(mContext, commentcount);
					relativeLayout_ring.addView(assessThan);
				}
				LayoutParams params = assessThan.getLayoutParams();
				params.height = LayoutParams.MATCH_PARENT;
				params.width = LayoutParams.MATCH_PARENT;
				assessThan.setLayoutParams(params);
				//百分比
				int sum = commentcount.goodcommnent + commentcount.badcomment
						+ commentcount.generalcomment;
				int good = (int) (((commentcount.goodcommnent) / (sum * 1.0f)) * 100);
				int bad = (int) (((commentcount.badcomment) / (sum * 1.0f)) * 100);
				int general =(int) (((commentcount.generalcomment) / (sum * 1.0f)) * 100);
				
				textView1.setText("好评  "+good+"%");
				textView2.setText("中评  "+general+"%");
				textView3.setText("差评  "+bad+"%");
				
				if (commentlevel==3) {
					tv_assess_number.setText("好评("+commentcount.goodcommnent+")");
				}else if(commentlevel==2){
					tv_assess_number.setText("中评("+commentcount.generalcomment+")");
				}else if(commentlevel==1){
					tv_assess_number.setText("差评("+commentcount.badcomment+")");
				}
				
			}
			progressBar_main.setVisibility(View.GONE);
//		}
		pullToRefreshListView.onRefreshComplete();

	}

	@Override
	public void processFailure() {
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.textView1:
			commentlevel=3;
			loadNetworkData();
			textView1.setTextSize(16);
			textView2.setTextSize(14);
			textView3.setTextSize(14);
			break;
		case R.id.textView2:
			commentlevel=2;
			loadNetworkData();
			textView2.setTextSize(16);
			textView1.setTextSize(14);
			textView3.setTextSize(14);
			break;
		case R.id.textView3:
			commentlevel=1;
			loadNetworkData();
			textView3.setTextSize(16);
			textView1.setTextSize(14);
			textView2.setTextSize(14);
			break;
		
		}
	}
}
