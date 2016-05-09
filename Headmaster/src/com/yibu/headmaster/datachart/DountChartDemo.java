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

import java.util.LinkedList;

import org.xclcharts.chart.DountChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.yibu.headmaster.bean.AssessBean.Commentcount;
import com.yibu.headmaster.utils.LogUtil;

/**
 * @ClassName DountChart01View
 * @Description 环形图例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class DountChartDemo extends DemoView {

	private String TAG = "DountChart01View";
	private DountChart chart = new DountChart();

	LinkedList<PieData> lPieData = new LinkedList<PieData>();

	// 评价比列的数据
	private Commentcount commentcount;

	public DountChartDemo(Context context, Commentcount commentcount) {
		super(context);
		this.commentcount = commentcount;
		initView();
	}

	public DountChartDemo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public DountChartDemo(Context context, AttributeSet attrs, int defStyle) {
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
			// chart.setPadding(ltrb[0], ltrb[1] + 100, ltrb[2], ltrb[3]);
			chart.setPadding(DensityUtil.dip2px(getContext(), 50), ltrb[1],
					ltrb[2], ltrb[3]);
			// 数据源
			chart.setDataSource(lPieData);

			// 标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);

			// 标题
			// chart.setTitle("环形图");
			// chart.addSubtitle("(XCL-Charts Demo)");
			// 显示key
			// chart.getPlotLegend().show();

			// 隐藏图例
			PlotLegend legend = chart.getPlotLegend();
			legend.hide();
			// legend.setType(XEnum.LegendType.COLUMN);
			// legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
			// legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
			// legend.showBox();
			// legend.getBox().setBorderRectType(XEnum.RectType.ROUNDRECT);

			// 图背景色
			chart.setApplyBackgroundColor(false);

			// 内环背景色

			chart.getInnerPaint().setColor(Color.rgb(53, 53, 53));

			// 显示边框线，并设置其颜色
			// chart.getArcBorderPaint().setColor(Color.YELLOW);

			// 可用这个修改环所占比例

			chart.setInnerRadius(0.2f);
			// chart.setInitialAngle(90.f);

			// 保存标签位置
			chart.saveLabelsPosition(XEnum.LabelSaveType.NONE);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		// 设置图表数据源
		// PieData(标签，百分比，在饼图中对应的颜色)
		// lPieData.add(new PieData("好评", "75%", 75, Color.rgb(1, 226, 182)));
		// lPieData.add(new PieData("中评", "20%", 20, Color.rgb(2, 171, 138)));
		// lPieData.add(new PieData("差评", "5%", 5, Color.rgb(4, 122, 100)));

		if (commentcount == null) {
			LogUtil.print("评论为" + commentcount);
		}
		if (commentcount != null) {
			int sum = commentcount.goodcommnent + commentcount.badcomment
					+ commentcount.generalcomment;
			if (sum == 0) {
				lPieData.add(new PieData("差评", "100%", 100, Color.rgb(4, 122,
						100)));
			} else {

				int good = (int) (((commentcount.goodcommnent) / (sum * 1.0f)) * 100);
				int bad = (int) (((commentcount.badcomment) / (sum * 1.0f)) * 100);
				int general = 100 - good - bad;

				LogUtil.print("总评论---" + (commentcount.goodcommnent)
						/ (sum * 1.0f) * 100);
				lPieData.add(new PieData("好评", good + "%", good, Color.rgb(1,
						226, 182)));
				lPieData.add(new PieData("中评", bad + "%", bad, Color.rgb(2,
						171, 138)));
				lPieData.add(new PieData("差评", general + "%", general, Color
						.rgb(4, 122, 100)));
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

}
