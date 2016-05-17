package com.yibu.headmaster.datachart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.XEnum.LabelBoxStyle;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.plot.PlotGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;

public class LineChartDemoOne extends DemoView {
	private String TAG = "MultiAxisChart03View";

	// 横向网格线
	private Paint mPaintGridLineHorizontal = null;
	// 竖向网格线
	private Paint mPaintGridLineVertical = null;
	// 用来显示面积图，左边及底部的轴
	private AreaChart chart = new AreaChart();
	// 标签集合
	private LinkedList<String> mLabels = new LinkedList<String>();
	// 数据集合
	private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();
	
	//批注
	private List<AnchorDataPoint> anchor = new ArrayList<AnchorDataPoint>();
	
	// 用来显示折线,右边及顶部的轴
	private LineChart chartLn = new LineChart();
	private LinkedList<LineData> chartData = new LinkedList<LineData>();

	// 招生表的数据
	private List<MoreDataBean> lineData;
	
	public LineChartDemoOne(Context context, List<MoreDataBean> lineData) {
		super(context);
		this.lineData = lineData;
		initView(); 

	}

	public LineChartDemoOne(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LineChartDemoOne(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	// 传数据
	// public void setData(List<Applystuentlist> applystuentlist) {
	//
	// this.applystuentlist = applystuentlist;
	// }

	public void initView() {
		
		chartLabels();
		chartDataSetLn();
		AnchorInit();
		chartRenderLn();
		// 綁定手势滑动事件
		this.bindTouch(this, chart);
		this.bindTouch(this, chartLn);
	}

	private void AnchorInit() {
		AnchorDataPoint point = new AnchorDataPoint();
		for (MoreDataBean data : lineData) {
			point.setAnchor(data.countY+"");
			anchor.add(point);
		}
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
//		chartLn.setChartRange(startX, startY, width, height)
		chartLn.setChartRange(8, 0, w, h);
	}

	// 横向网格线

	// 竖向网格线

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
			
			chartLn.setAnchorDataPoint(anchor);
			// 禁止缩放
			chartLn.disableScale();

			// 背景网格
			chartLn.getPlotGrid().showHorizontalLines();
			//设置横线颜色
			chartLn.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#e8e8ed"));
			
//			chartLn.getPlotGrid().showVerticalLines();
			
			// chartLn.getPlotGrid().showEvenRowBgColor();
			// chartLn.getPlotGrid().showOddRowBgColor();
//			chartLn.getPlotGrid().getHorizontalLinePaint();
//			chartLn.getPlotGrid().getVerticalLinePaint();
			// chartLn.getPlotGrid().setEvenRowsFillVisible(true);
			

			int max = Integer.MIN_VALUE;
			for (int i = 0; i < lineData.size(); i++) {
				if (lineData.get(i).countY > max) {
					max = lineData.get(i).countY;
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

			// 隐藏轴线和tick
			chartLn.getDataAxis().hideAxisLine();
			chartLn.getCategoryAxis().hideAxisLine();
			chartLn.getDataAxis().hideTickMarks();
			// chartLn.getCategoryAxis().hideTickMarks();

			// chartLn.getDataAxis().setTickLabelRotateAngle(-90);
			chartLn.getDataAxis().getTickLabelPaint()
					.setColor(Color.rgb(61, 139, 255));
			
//			chartLn.getDataAxis().getTickLabelPaint().setTextSize(38);
			
			chartLn.getDataAxis().getTickLabelPaint().setTextSize(11*HeadmasterApplication.density);
			
			chartLn.getCategoryAxis().getTickLabelPaint().setTextSize(11*HeadmasterApplication.density);
			chartLn.getCategoryAxis().getTickLabelPaint()
					.setColor(getResources().getColor(R.color.text_color_light_black));
			// 隐藏图列
			chartLn.getPlotLegend().hide();
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
			// 图例显示在正下方
			// chartLn.getPlotLegend()
			// .setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			//
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
		// dataSeries1.add(10d);
		// dataSeries1.add(12d);

		if (lineData != null) {
			for (MoreDataBean data : lineData) {
				dataSeries1.add((double) data.countY);
			}

		}

		LineData lineData1 = new LineData("直线", dataSeries1, Color.rgb(61, 139,
				255));
		lineData1.setDotStyle(XEnum.DotStyle.RING);
		lineData1.getDotLabelPaint().setColor(Color.rgb(232, 0, 49));
		//显示标签
		lineData1.setLabelVisible(false);
//		lineData1.getLabelOptions().setLabelBoxStyle(LabelBoxStyle.CAPROUNDRECT);
		// lineData1.getPlotLine().getPlotDot().setRingInnerColor(Color.GREEN);
		lineData1.getLabelOptions().setLabelBoxStyle(
				XEnum.LabelBoxStyle.CAPRECT);
		
		
		lineData1.getLinePaint().setStrokeWidth(2);
		lineData1.getDotPaint().setStrokeWidth(2);
		lineData1.setDotRadius((int)(3*HeadmasterApplication.density));
		chartData.add(lineData1);

	}

	// X轴数据
	public void chartLabels() {

		if (lineData != null) {
			for (MoreDataBean data : lineData) {
				mLabels.add(data.timeX);
			}

		}
		//强制 补齐7个
//		if (mLabels.size() < 7) {
//			int temp = (7 - mLabels.size());
//			for (int j = 0; j < temp; j++) {
//				mLabels.add("");
//			}
//		}

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
