/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.jzjf.headmaster.datachart;

import java.util.ArrayList;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;

import com.jzjf.headmaster.bean.AssessBean.Commentcount;
import com.jzjf.headmaster.utils.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @ClassName PieChart01View
 * @Description 饼图的例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class PieChart01View extends DemoView implements Runnable {

	private String TAG = "PieChart01View";
	private PieChart chart = new PieChart();
	private ArrayList<PieData> chartData = new ArrayList<PieData>();
	private int mSelectedID = -1;
	// 评价比列的数据
	private Commentcount commentcount;

	public PieChart01View(Context context, Commentcount commentcount) {
		super(context);
		this.commentcount = commentcount;
		initView();
	}

	public PieChart01View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PieChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	// 传数据
	public void setData(Commentcount commentcount) {

		this.commentcount = commentcount;
	}
	private void initView() {
		chartDataSet();
		chartRender();

		// 綁定手势滑动事件
		this.bindTouch(this, chart);
		new Thread(this).start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {
			// 设置绘图区默认缩进px值
			int[] ltrb = getPieDefaultSpadding();
			float right = DensityUtil.dip2px(getContext(), 100);
			chart.setPadding(ltrb[0], ltrb[1], right, ltrb[3]);

			// 设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
			// chart.setInitialAngle(90);

			// 标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);

			// 标题
			chart.setTitle("饼图-Pie Chart");
			chart.addSubtitle("(XCL-Charts Demo)");
//			chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			// chart.setDataSource(chartData);
			// 激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();

			// 设置允许的平移模式
			// 关闭平移模式
			chart.disablePanMode();
			// 关闭缩放
			chart.disableScale();

			// 显示图例
//			PlotLegend legend = chart.getPlotLegend();
//			legend.show();
//			legend.setType(XEnum.LegendType.COLUMN);
//			legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
//			legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
//			legend.showBox();

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		if (commentcount == null) {
			LogUtil.print("评论为" + commentcount);
		}
		if (commentcount != null) {
			int sum = commentcount.goodcommnent + commentcount.badcomment
					+ commentcount.generalcomment;
			if (sum == 0) {
				chartData.add(new PieData("", "暂无评价", 100, Color.rgb(140, 140, 140)));
			} else {
				int bad = (int) (commentcount.badcomment * 100 / sum);
				int general = (int) (commentcount.generalcomment * 100 / sum);
				int good = 100 - bad - general;
				if(commentcount.goodcommnent == 0) {
					good = 0;
				}
				
				PieData goodPie = new PieData("3", good + "%", good, Color.rgb(123, 214, 92));
				PieData generalPie = new PieData("2", general + "%", general, Color.rgb(232, 0, 49));
				PieData badPie = new PieData("1", bad + "%", bad, Color.rgb(61, 139, 255));
				
				goodPie.setSelected(false);
				generalPie.setSelected(false);
				badPie.setSelected(false);
				
//				int selectIndex = commentcount.selectIndex;
//				if(selectIndex == 1) { // bad
//					badPie.setSelected(true);
//				} else if(selectIndex == 2) { // general
//					generalPie.setSelected(true);
//				} else if(selectIndex == 3) { // good
//					goodPie.setSelected(true);
//				}
				
				chartData.add(goodPie);
				chartData.add(generalPie);
				chartData.add(badPie);
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public void run() {
		try {
			chartAnimation();
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}

	private void chartAnimation() {
		try {

			chart.setDataSource(chartData);
			int count = 360 / 10;

			for (int i = 1; i < count; i++) {
				Thread.sleep(40);

				chart.setTotalAngle(10 * i);

				// 激活点击监听
				if (count - 1 == i) {
					chart.setTotalAngle(360);
					chart.ActiveListenItemClick();
					// 显示边框线，并设置其颜色
					chart.getArcBorderPaint().setColor(Color.WHITE);
					chart.getArcBorderPaint().setStrokeWidth(1);
				}

				postInvalidate();
			}

		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}

	}

	/*
	 * 另一种动画 private void chartAnimation() { try {
	 * 
	 * float sum = 0.0f; int count = chartData.size(); for(int i=0;i< count
	 * ;i++) { Thread.sleep(150);
	 * 
	 * ArrayList<PieData> animationData = new ArrayList<PieData>();
	 * 
	 * sum = 0.0f;
	 * 
	 * for(int j=0;j<=i;j++) { animationData.add(chartData.get(j)); sum =
	 * (float) MathHelper.getInstance().add( sum ,
	 * chartData.get(j).getPercentage()); }
	 * 
	 * animationData.add(new PieData("","", MathHelper.getInstance().sub(100.0f
	 * , sum), Color.argb(1, 0, 0, 0))); chart.setDataSource(animationData);
	 * 
	 * //激活点击监听 if(count - 1 == i) { chart.ActiveListenItemClick();
	 * //显示边框线，并设置其颜色 chart.getArcBorderPaint().setColor(Color.YELLOW);
	 * chart.getArcBorderPaint().setStrokeWidth(3); }
	 * 
	 * postInvalidate(); }
	 * 
	 * } catch(Exception e) { Thread.currentThread().interrupt(); }
	 * 
	 * }
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (chart.isPlotClickArea(event.getX(), event.getY())) {
				triggerClick(event.getX(), event.getY());
			}
		}
		return true;
	}

	// 触发监听
	private void triggerClick(float x, float y) {
		if (!chart.getListenItemClickStatus())
			return;

		ArcPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;
		/*
		 * PieData pData = chartData.get(record.getDataID());
		 * Toast.makeText(this.getContext(), " key:" + pData.getKey() +
		 * " Label:" + pData.getLabel() , Toast.LENGTH_SHORT).show();
		 */

		// 用于处理点击时弹开，再点时弹回的效果
		PieData pData = chartData.get(record.getDataID());
		if (record.getDataID() == mSelectedID) {
			boolean bStatus = chartData.get(mSelectedID).getSelected();
			chartData.get(mSelectedID).setSelected(!bStatus);
		} else {
			if (mSelectedID >= 0)
				chartData.get(mSelectedID).setSelected(false);
			pData.setSelected(true);
		}
		mSelectedID = record.getDataID();
		
		
		this.refreshChart();
		
		

		/*
		 * boolean isInvaldate = true; for(int i=0;i < chartData.size();i++) {
		 * PieData cData = chartData.get(i); if(i == record.getDataID()) {
		 * if(cData.getSelected()) { isInvaldate = false; break; }else{
		 * cData.setSelected(true); } }else cData.setSelected(false); }
		 * if(isInvaldate)this.invalidate();
		 */

	}

}