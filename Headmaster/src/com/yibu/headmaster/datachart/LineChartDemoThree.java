package com.yibu.headmaster.datachart;

import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.utils.LogUtil;

public class LineChartDemoThree extends DemoView {
	private String TAG = "MultiAxisChart03View";

	// 用来显示面积图，左边及底部的轴
	private AreaChart chart = new AreaChart();
	// 标签集合
	private LinkedList<String> mLabels = new LinkedList<String>();
	// 数据集合
	private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();

	// 用来显示折线,右边及顶部的轴
	private LineChart chartLn = new LineChart();
	private LinkedList<LineData> chartData = new LinkedList<LineData>();

	private List<MoreDataBean> goodcommentlist;
	private List<MoreDataBean> generalcommentlist;
	private List<MoreDataBean> badcommentlist;
	private List<MoreDataBean> complaintlist;

	public LineChartDemoThree(Context context,
			List<MoreDataBean> goodcommentlist,
			List<MoreDataBean> generalcommentlist,
			List<MoreDataBean> badcommentlist, List<MoreDataBean> complaintlist) {
		super(context);
		this.goodcommentlist = goodcommentlist;
		this.generalcommentlist = generalcommentlist;
		this.badcommentlist = badcommentlist;
		this.complaintlist = complaintlist;

		LogUtil.print("generalcommentlist" + generalcommentlist.size());
		initView();

	}

	public LineChartDemoThree(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LineChartDemoThree(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	// 传数据
	// public void setData(List<Goodcommentlist> goodcommentlist,
	// List<Generalcommentlist> generalcommentlist,
	// List<Badcommentlist> badcommentlist,
	// List<Complaintlist> complaintlist) {
	//
	// this.goodcommentlist = goodcommentlist;
	// this.generalcommentlist = generalcommentlist;
	// this.badcommentlist = badcommentlist;
	// this.complaintlist = complaintlist;
	// }

	private void initView() {

		chartLabels();
		chartDataSetLn();
		chartRenderLn();
		// 綁定手势滑动事件
		this.bindTouch(this, chart);
		this.bindTouch(this, chartLn);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		// chart.setChartRange(w, h);
		chartLn.setChartRange(w, h);
		// chartLnAxes.setChartRange(w, h);

		float left = DensityUtil.dip2px(getContext(), 52);
		float top = DensityUtil.dip2px(getContext(), 62);

		float piewidth = Math.min(w, h) / 4;// 1.5f;

		// chartPie.setChartRange(left, top, piewidth, piewidth);
	}

	private void chartRenderLn() {
		try {

			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();

			float left = DensityUtil.dip2px(getContext(), 20); // left 40
			float right = DensityUtil.dip2px(getContext(), 10); // right 20
			chartLn.setPadding(left, ltrb[1], right, ltrb[3]); // ltrb[2]

			// 设定数据源
			chartLn.setCategories(mLabels);
			chartLn.setDataSource(chartData);
			// 禁止缩放
			chartLn.disableScale();

			// 背景网格
			chartLn.getPlotGrid().showHorizontalLines();
			chartLn.getPlotGrid().showVerticalLines();
			// chartLn.getPlotGrid().showEvenRowBgColor();
			// chartLn.getPlotGrid().showOddRowBgColor();

			int max = Integer.MIN_VALUE;
			for (int i = 0; i < goodcommentlist.size(); i++) {
				if (goodcommentlist.get(i).countY > max) {
					max = goodcommentlist.get(i).countY;
				}
			}
			for (int i = 0; i < generalcommentlist.size(); i++) {
				if (generalcommentlist.get(i).countY > max) {
					max = generalcommentlist.get(i).countY;
				}
			}
			for (int i = 0; i < badcommentlist.size(); i++) {
				if (badcommentlist.get(i).countY > max) {
					max = badcommentlist.get(i).countY;
				}
			}
			for (int i = 0; i < complaintlist.size(); i++) {
				if (complaintlist.get(i).countY > max) {
					max = complaintlist.get(i).countY;
				}
			}

			int axisMax = 8;
			if ((max % 4 == 0) && (max != 0)) {
				axisMax = max;
			} else {
				axisMax = max + 4 - max % 4;
			}

			// 数据轴最大值
			chartLn.getDataAxis().setAxisMax(axisMax);
			// 数据轴刻度间隔
			chartLn.getDataAxis().setAxisSteps(axisMax / 4);

			// 仅横向平移
			chartLn.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

			// 隐藏X,Y轴线
			chartLn.getDataAxis().hideAxisLine();
			chartLn.getDataAxis().hideTickMarks();

			// chartLn.getCategoryAxis().hideTickMarks();
			chartLn.getCategoryAxis().hideAxisLine();

			// chartLn.getDataAxis().setTickLabelRotateAngle(-90);
			chartLn.getDataAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));
			chartLn.getCategoryAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));

			// 调整轴显示位置

			chartLn.setCategoryAxisLocation(XEnum.AxisLocation.BOTTOM);

			// 把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
			PlotGrid plot = chart.getPlotGrid();
			chartLn.getDataAxis()
					.getAxisPaint()
					.setStrokeWidth(
							plot.getHorizontalLinePaint().getStrokeWidth());
			chartLn.getCategoryAxis()
					.getAxisPaint()
					.setStrokeWidth(
							plot.getHorizontalLinePaint().getStrokeWidth());

			chartLn.getDataAxis().getAxisPaint()
					.setColor(plot.getHorizontalLinePaint().getColor());
			chartLn.getCategoryAxis().getAxisPaint()
					.setColor(plot.getHorizontalLinePaint().getColor());

			chartLn.getDataAxis().getTickMarksPaint()
					.setColor(plot.getHorizontalLinePaint().getColor());
			chartLn.getCategoryAxis().getTickMarksPaint()
					.setColor(plot.getHorizontalLinePaint().getColor());

			chartLn.setXCoordFirstTickmarksBegin(true);
			// 图例隐藏
			chartLn.getPlotLegend().hide();
			// .setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			// chartLn.getPlotLegend().setHorizontalAlign(
			// XEnum.HorizontalAlign.CENTER);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSetLn() {
		// 好评
		LinkedList<Double> dataSeries1 = new LinkedList<Double>();

		if (goodcommentlist != null) {
			for (MoreDataBean good : goodcommentlist) {
				dataSeries1.add((double) good.countY);
			}

		}
		LineData lineData1 = new LineData("好评", dataSeries1, Color.rgb(0, 255,
				204));
		lineData1.setDotStyle(XEnum.DotStyle.DOT);
		lineData1.getDotLabelPaint().setColor(Color.rgb(0, 255, 204));
		lineData1.setLabelVisible(false);
		// lineData1.getLabelOptions().setLabelBoxStyle(
		// XEnum.LabelBoxStyle.CAPROUNDRECT);

		// 中评
		LinkedList<Double> dataSeries2 = new LinkedList<Double>();
		if (generalcommentlist != null) {
			for (MoreDataBean general : generalcommentlist) {
				dataSeries2.add((double) general.countY);
			}

		}
		LineData lineData2 = new LineData("中评", dataSeries2, Color.rgb(243,
				173, 84));
		lineData2.setDotStyle(XEnum.DotStyle.DOT);
		lineData2.getPlotLine().getDotPaint().setColor(Color.rgb(243, 173, 84));
		lineData2.setLabelVisible(false);
		// lineData2.getLabelOptions().setLabelBoxStyle(
		// XEnum.LabelBoxStyle.CAPRECT);

		// 差评
		LinkedList<Double> dataSeries3 = new LinkedList<Double>();
		if (badcommentlist != null) {
			for (MoreDataBean bad : badcommentlist) {
				dataSeries3.add((double) bad.countY);
			}

		}
		LineData lineData3 = new LineData("差评", dataSeries3, Color.rgb(255,
				102, 58));
		lineData3.setDotStyle(XEnum.DotStyle.DOT);
		lineData3.setLabelVisible(false);
		lineData3.getPlotLine().getPlotDot()
				.setRingInnerColor(Color.rgb(255, 102, 58));
		// lineData3.getLabelOptions().setLabelBoxStyle(
		// XEnum.LabelBoxStyle.CAPRECT);

		// 投诉
		LinkedList<Double> dataSeries4 = new LinkedList<Double>();
		if (complaintlist != null) {
			for (MoreDataBean complaint : complaintlist) {
				dataSeries4.add((double) complaint.countY);
			}

		}
		LineData lineData4 = new LineData("投诉", dataSeries4, Color.rgb(237, 28,
				36));
		lineData4.setDotStyle(XEnum.DotStyle.DOT);
		lineData4.setLabelVisible(false);
		lineData4.getPlotLine().getPlotDot()
				.setRingInnerColor(Color.rgb(237, 28, 36));

		chartData.add(lineData1);
		chartData.add(lineData2);
		chartData.add(lineData3);
		chartData.add(lineData4);
	}

	// X轴数据
	private void chartLabels() {

		int flag = -1; // 1好评 2 中评 3 差评 4投诉

		// 找出最大值
		int[] temp = new int[4];
		temp[0] = goodcommentlist.size();
		temp[1] = generalcommentlist.size();
		temp[2] = badcommentlist.size();
		temp[3] = complaintlist.size();
		int min = Integer.MIN_VALUE;
		for (int i = 0; i < temp.length; i++) {

			if (temp[i] > min) {
				flag = i;
				min = temp[i];
			}
		}

		flag = 1;
		switch (flag) {
		case 0:

			if (goodcommentlist != null) {
				for (MoreDataBean goodcomment : goodcommentlist) {
					mLabels.add(goodcomment.timeX);
				}

			}
			break;
		case 1:

			if (generalcommentlist != null) {
				for (MoreDataBean generalcomment : generalcommentlist) {
					mLabels.add(generalcomment.timeX);
				}

			}
			break;
		case 2:
			if (badcommentlist != null) {
				for (MoreDataBean badcomment : badcommentlist) {
					mLabels.add(badcomment.timeX);
				}

			}

			break;
		case 3:

			if (complaintlist != null) {
				for (MoreDataBean complaint : complaintlist) {
					mLabels.add(complaint.timeX);
				}

			}
			break;

		default:
			break;
		}

		if (mLabels.size() < 7) {
			int temp2 = (7 - mLabels.size());
			for (int j = 0; j < temp2; j++) {
				mLabels.add("");
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
			chartLn.render(canvas);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
