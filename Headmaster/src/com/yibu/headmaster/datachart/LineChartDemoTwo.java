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

import com.yibu.headmaster.bean.MoreDataBean.Reservationstudentlist;

public class LineChartDemoTwo extends DemoView {
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

	// 约课表的数据
	private List<Reservationstudentlist> reservationstudentlist;

	public LineChartDemoTwo(Context context,
			List<Reservationstudentlist> reservationstudentlist) {
		super(context);
		this.reservationstudentlist = reservationstudentlist;
		initView();
	}

	public LineChartDemoTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LineChartDemoTwo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	// 传数据
	public void setData(List<Reservationstudentlist> reservationstudentlist) {

		this.reservationstudentlist = reservationstudentlist;
	}

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

		float left = DensityUtil.dip2px(getContext(), 42);
		float top = DensityUtil.dip2px(getContext(), 62);

		float piewidth = Math.min(w, h) / 4;// 1.5f;

		// chartPie.setChartRange(left, top, piewidth, piewidth);
	}

	private void chartRenderLn() {
		try {

			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();

			float left = DensityUtil.dip2px(getContext(), 40); // left 40
			float right = DensityUtil.dip2px(getContext(), 40); // right 20
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
			// 数据轴最大值
			chartLn.getDataAxis().setAxisMax(28);
			// 数据轴刻度间隔
			chartLn.getDataAxis().setAxisSteps(7);

			// 仅横向平移
			chartLn.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

			// 隐藏图列
			chartLn.getPlotLegend().hide();

			// 隐藏X,Y轴线
			chartLn.getDataAxis().hideAxisLine();

			chartLn.getCategoryAxis().hideAxisLine();

			chartLn.getDataAxis().setTickLabelRotateAngle(-90);
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

			// 图例显示在正下方
			// chartLn.getPlotLegend()
			// .setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			// chartLn.getPlotLegend().setHorizontalAlign(
			// XEnum.HorizontalAlign.CENTER);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSetLn() {
		// Line 1
		LinkedList<Double> dataSeries1 = new LinkedList<Double>();
		// dataSeries1.add(5d);

		if (reservationstudentlist != null) {
			for (Reservationstudentlist reservationstudent : reservationstudentlist) {
				dataSeries1.add((double) reservationstudent.studentcount);
			}

		}

		LineData lineData1 = new LineData("直线", dataSeries1, Color.rgb(1, 226,
				182));
		lineData1.setDotStyle(XEnum.DotStyle.RING);
		lineData1.getDotLabelPaint().setColor(Color.rgb(1, 226, 182));
		lineData1.setLabelVisible(true);
		// lineData1.getPlotLine().getPlotDot().setRingInnerColor(Color.GREEN);
		lineData1.getLabelOptions().setLabelBoxStyle(
				XEnum.LabelBoxStyle.CAPRECT);

		// Line 2
		LinkedList<Double> dataSeries2 = new LinkedList<Double>();
		// dataSeries2.add((double) 0);

		if (reservationstudentlist != null) {
			for (Reservationstudentlist reservationstudent : reservationstudentlist) {
				dataSeries2.add((double) reservationstudent.studentcount);
			}
		}
		LineData lineData2 = new LineData("圆环", dataSeries2, Color.rgb(1, 226,
				182));
		lineData2.setDotStyle(XEnum.DotStyle.RING);
		lineData2.getPlotLine().getDotPaint().setColor(Color.rgb(1, 226, 182));
		lineData2.setLabelVisible(true);
		// lineData2.getPlotLine().getPlotDot().setRingInnerColor(Color.GREEN);
		lineData2.getLabelOptions().setLabelBoxStyle(
				XEnum.LabelBoxStyle.CAPRECT);

		chartData.add(lineData1);
		chartData.add(lineData2);

	}

	// X轴数据
	private void chartLabels() {
		// mLabels.add("7:00");
		if (reservationstudentlist != null) {
			for (Reservationstudentlist reservationstudent : reservationstudentlist) {
				mLabels.add(reservationstudent.hour + ":00");
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
