package com.yibu.headmaster.datachart;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart3D;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @ClassName Bar3DChart01View
 * @Description 3D柱形图例子
 * @author XiongChuanLiang<br/>
 * 
 */
public class BarChartDemo extends DemoView {

	private String TAG = "Bar3DChart01View";
	private BarChart3D chart = new BarChart3D();

	// 标签轴
	private List<String> chartLabels = new LinkedList<String>();
	// 数据轴
	private List<BarData> BarDataset = new LinkedList<BarData>();

	Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

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
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {

			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(getContext(), 50), ltrb[1],
					ltrb[2], ltrb[3] + DensityUtil.dip2px(getContext(), 25));

			// 显示边框
			// chart.showRoundBorder();

			// 数据源
			chart.setDataSource(BarDataset);
			chart.setCategories(chartLabels);
			// 禁止缩放
			chart.disableScale();
			// 坐标系
			chart.getDataAxis().setAxisMax(28);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(7);
			// chart.getCategoryAxis().setAxisTickLabelsRotateAngle(-45f);

			// 隐藏轴线和tick
			chart.getDataAxis().hideAxisLine();

			chart.getCategoryAxis().hideAxisLine();

			// 标题
			chart.setTitle("教授授课");
			chart.addSubtitle("课时");
			chart.setTitleAlign(XEnum.HorizontalAlign.LEFT);

			// 背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			// chart.getPlotGrid().showEvenRowBgColor();
			// chart.getPlotGrid().showOddRowBgColor();

			// 定义数据轴标签显示格式
			chart.getDataAxis().setTickLabelRotateAngle(-45);
			chart.getDataAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));
			chart.getCategoryAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 226, 182));
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return (label + "节");
				}

			});

			/*
			 * // 设置标签轴标签交错换行显示 chart.getCategoryAxis().setLabelLineFeed(
			 * XEnum.LabelLineFeed.EVEN_ODD);
			 */

			// 定义标签轴标签显示格式

			// chart.getCategoryAxis().setAxisLineStyle(XEnum.AxisLineStyle.NONE);
			chart.getCategoryAxis().setLabelFormatter(
					new IFormatterTextCallBack() {

						@Override
						public String textFormatter(String value) {
							// TODO Auto-generated method stub
							return value;
						}

					});
			// 定义柱形上标签显示格式
			chart.getBar().setItemLabelVisible(true);
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

			// 仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

			// 扩展横向显示范围
			// chart.getPlotArea().extWidth(200f);

			// 标签文字与轴间距
			chart.getCategoryAxis().setTickLabelMargin(5);

			// 不使用精确计算，忽略Java计算误差
			chart.disableHighPrecision();

			// 设置轴标签字体大小
			chart.getDataAxis().getTickLabelPaint().setTextSize(26);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void chartDataSet() {
		// 标签对应的柱形数据集
		List<Double> dataSeriesA = new LinkedList<Double>();
		dataSeriesA.add(20d);
		dataSeriesA.add(25d);
		dataSeriesA.add(14d);
		dataSeriesA.add(15d);

		BarDataset.add(new BarData("课时", dataSeriesA, Color.rgb(1, 226, 182)));

	}

	private void chartLabels() {
		chartLabels.add("1");
		chartLabels.add("2");
		chartLabels.add("3");
		chartLabels.add("1");
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
		if (!chart.getListenItemClickStatus())
			return;

		BarPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;

		BarData bData = BarDataset.get(record.getDataID());
		Double bValue = bData.getDataSet().get(record.getDataChildID());

		// 在点击处显示tooltip
		mPaintToolTip.setColor(Color.WHITE);
		chart.getToolTip().getBackgroundPaint()
				.setColor(Color.rgb(1, 226, 182));
		chart.getToolTip().getBorderPaint().setColor(Color.RED);
		chart.getToolTip().setCurrentXY(x, y);
		chart.getToolTip().addToolTip(
				" Current Value:" + Double.toString(bValue), mPaintToolTip);
		chart.getToolTip().getBackgroundPaint().setAlpha(100);
		this.invalidate();
	}

}
