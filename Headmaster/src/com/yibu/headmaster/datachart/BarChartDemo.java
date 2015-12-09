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
package com.yibu.headmaster.datachart;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @ClassName BarChart02View
 * @Description 柱形图例子(横向)
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class BarChartDemo extends DemoView {

	private static final String TAG = "BarChart02View";
	private BarChart chart = new BarChart();

	// 标签轴
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();

	public BarChartDemo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public BarChartDemo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public BarChartDemo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();

		// 綁定手势滑动事件
		this.bindTouch(this, chart);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		if (null != chart)
			chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(getContext(), 50), ltrb[1],
					ltrb[2], ltrb[3]);

			// 数据源
			chart.setDataSource(chartData);
			chart.setCategories(chartLabels);
			// 隐藏轴线和tick
			chart.getDataAxis().hideAxisLine();
			chart.getCategoryAxis().hideAxisLine();

			// 禁止缩放
			chart.disableScale();
			// 仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
			// // 轴标题
			// chart.getAxisTitle().setLeftTitle("所售商品");
			// chart.getAxisTitle().setLowerTitle("纯利润(天)");
			// chart.getAxisTitle().setRightTitle("生意兴隆通四海,财源茂盛达三江。");
			// 隐藏Key
			chart.getPlotLegend().hide();

			// 数据轴
			chart.getDataAxis().setAxisMax(28);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(7);

			chart.getDataAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));
			chart.getCategoryAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));

			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					String tmp = value + "节";
					return tmp;
				}

			});

			// 网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			// chart.getPlotGrid().showEvenRowBgColor();
			// 柱形和标签居中方式
			chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

			// 标签轴文字旋转-45度
			// chart.getCategoryAxis().setTickLabelRotateAngle(-45f);
			// 横向显示柱形
			chart.setChartDirection(XEnum.Direction.VERTICAL);
			// 在柱形顶部显示值
			chart.getBar().setItemLabelVisible(true);
			chart.getBar().getItemLabelPaint().setTextSize(22);

			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(value).toString();
					return label;
				}
			});

			// 激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();

			/*
			 * chart.setDataAxisPosition(XEnum.DataAxisPosition.BOTTOM);
			 * chart.getDataAxis
			 * ().setVerticalTickPosition(XEnum.VerticalAlign.BOTTOM);
			 * 
			 * chart.setCategoryAxisPosition(XEnum.CategoryAxisPosition.LEFT);
			 * chart.getCategoryAxis().setHorizontalTickAlign(Align.LEFT);
			 */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}

	}

	private void chartDataSet() {
		// 标签对应的柱形数据集
		List<Double> dataSeriesA = new LinkedList<Double>();
		dataSeriesA.add((double) 20);
		dataSeriesA.add((double) 25);
		dataSeriesA.add((double) 15);
		dataSeriesA.add((double) 18);
		dataSeriesA.add((double) 16);
		BarData BarDataA = new BarData("小熊", dataSeriesA,
				Color.rgb(1, 226, 182));

		chartData.add(BarDataA);
	}

	private void chartLabels() {
		chartLabels.add("2");
		chartLabels.add("3");
		chartLabels.add("2");
		chartLabels.add("1");
		chartLabels.add("2");

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
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			triggerClick(event.getX(), event.getY());
		}
		return true;
	}

	// 触发监听
	private void triggerClick(float x, float y) {
		BarPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;

		BarData bData = chartData.get(record.getDataID());
		Double bValue = bData.getDataSet().get(record.getDataChildID());

		Toast.makeText(
				this.getContext(),
				"info:" + record.getRectInfo() + " Key:" + bData.getKey()
						+ " Current Value:" + Double.toString(bValue),
				Toast.LENGTH_SHORT).show();

		chart.showFocusRectF(record.getRectF());
		chart.getFocusPaint().setStyle(Style.STROKE);
		chart.getFocusPaint().setStrokeWidth(3);
		chart.getFocusPaint().setColor(Color.rgb(1, 226, 182));
		this.invalidate();
	}

}
