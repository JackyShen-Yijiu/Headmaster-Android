package com.jzjf.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzjf.headmaster.R;
import com.jzjf.headmaster.adapter.AssessAdapter;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.base.BasePagerFragment;
import com.jzjf.headmaster.bean.AssessBean;
import com.jzjf.headmaster.bean.AssessBean.Commentcount;
import com.jzjf.headmaster.bean.AssessBean.Commentlist;
import com.jzjf.headmaster.datachart.PieChart01View;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.ToastUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AssessDetailPager extends BasePagerFragment {

	private View view;
	private ArrayList<Commentlist> listAssess = new ArrayList<Commentlist>();
	private AssessAdapter adapterAssess;
	private int curpage = 1;
	private View viewHeader;
	// 刷新
	private ProgressBar progressBar_main;
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_assess;
	private RelativeLayout relativeLayout_ring;

	private PieChart01View assessThan;// 评价比列

	private int searchtype = 1; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年 6： 上周 7 上月
	private int commentlevel = 3;
	
	private boolean hasMoreData = true;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private ImageView black_page_iv;
	private LinearLayout black_page_ll;
	private TextView black_page_tv;
	private TextView tv_assess_number;

	public AssessDetailPager(Context context, int searchtype, int commentlevel) {
		super(context);
		
		this.commentlevel = commentlevel;
		
		switch (searchtype) {
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
		textView1=(TextView)viewHeader.findViewById(R.id.textView1);
		textView2=(TextView)viewHeader.findViewById(R.id.textView2);
		textView3=(TextView)viewHeader.findViewById(R.id.textView3);
		list_assess.addHeaderView(viewHeader);
		black_page_iv=(ImageView)view.findViewById(R.id.black_page_iv);
		black_page_ll=(LinearLayout)view.findViewById(R.id.black_page_ll);
		black_page_tv=(TextView)view.findViewById(R.id.black_page_tv);
		
		tv_assess_number = (TextView)view.findViewById(R.id.tv_assess_number);
		
		return view;
	}

	@Override
	public void initData() {
		adapterAssess = new AssessAdapter(mContext, listAssess);
		list_assess.setAdapter(adapterAssess);
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

		ApiHttpClient.get(ApiHttpClient.COLLECT_COMMENT
				+ "?index=" + curpage + "&count=10&searchtype="
				+ searchtype + "&commentlevel=-1", handler);
	}

	@Override
	public void process(String data) {
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
				
			} else {
				hasMoreData = false;
			}
		} else {
			listAssess.addAll(commentlist);
			adapterAssess.notifyDataSetChanged();
		}
		if (assessBean != null) {
			Commentcount commentcount = assessBean.commentcount;
			commentcount.selectIndex = commentlevel;
			
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
			if(sum > 0) {
				int bad = (int) (commentcount.badcomment * 100 / sum);
				int general = (int) (commentcount.generalcomment * 100 / sum);
				int good = 100 - bad - general;
				if(commentcount.goodcommnent == 0) {
					good = 0;
				}
				
				textView1.setText("好评  " + good +  "%");
				textView2.setText("中评  " + general + "%");
				textView3.setText("差评  " + bad + "%");
		
			} else {
				textView1.setText("好评  0%");
				textView2.setText("中评  0%");
				textView3.setText("差评  0%");
			}
			
			textView1.setTextSize(14);
			textView2.setTextSize(14);
			textView3.setTextSize(14);
			
//			if (commentlevel == 3) {
//				tv_assess_number.setText("好评("+commentcount.goodcommnent+")");
//				textView1.setTextSize(16);
//			} else if(commentlevel == 2){
//				tv_assess_number.setText("中评("+commentcount.generalcomment+")");
//				textView2.setTextSize(16);
//			} else if(commentlevel == 1){
//				tv_assess_number.setText("差评("+commentcount.badcomment+")");
//				textView3.setTextSize(16);
//			}
			
			tv_assess_number.setText("评价(" + sum + ")");
			
		}
		
		progressBar_main.setVisibility(View.GONE);
		
		pullToRefreshListView.onRefreshComplete();
	}

	@Override
	public void processFailure() {
		progressBar_main.setVisibility(View.GONE);
		pullToRefreshListView.setVisibility(View.GONE);
		black_page_ll.setVisibility(View.VISIBLE);
		black_page_iv.setBackgroundResource(R.drawable.net_null);
		black_page_tv.setText("网络开小差了");
	}
	
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.textView1:
//			loadNetworkData();
//			textView1.setTextSize(16);
//			textView2.setTextSize(14);
//			textView3.setTextSize(14);
//			
//			commentlevel = 3;
//			break;
//		case R.id.textView2:
//			loadNetworkData();
//			textView2.setTextSize(16);
//			textView1.setTextSize(14);
//			textView3.setTextSize(14);
//			
//			commentlevel = 2;
//			break;
//		case R.id.textView3:
//			loadNetworkData();
//			textView3.setTextSize(16);
//			textView1.setTextSize(14);
//			textView2.setTextSize(14);
//			
//			commentlevel = 1;
//			break;
//		}
//		
//		curpage = 1;
//		this.loadNetworkData();
//	}
}
