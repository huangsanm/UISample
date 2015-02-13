package com.huashengmi.ui.android.ui.view.chart;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;

import java.text.DecimalFormat;

public class ChartActivity extends BaseActivity {

    /**
     * Line
     */
    private final static int LINE_MAX = 20;
    private final static int LINE_MIN = 0;
    private String[] lineLabels = {"", "ANT", "GNU", "OWL", "APE", "JAY", ""};
    private float[] lineValues = {12f, 4f, 18f, 9f, 0f, 15f, 5f};
    private static LineChartView mLineChart;
    private Paint mLineGridPaint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initLineChart();

        updateLineChart();
    }

    private void initLineChart(){
        mLineChart = (LineChartView) findViewById(R.id.linechart);
        mLineChart.setOnEntryClickListener(lineEntryListener);
        mLineChart.setOnClickListener(lineClickListener);

        mLineGridPaint = new Paint();
        mLineGridPaint.setColor(getResources().getColor(android.R.color.white));
        mLineGridPaint.setPathEffect(new DashPathEffect(new float[] {5,5}, 0));
        mLineGridPaint.setStyle(Paint.Style.STROKE);
        mLineGridPaint.setAntiAlias(true);
        mLineGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
    }

    private void updateLineChart(){

        mLineChart.reset();

        LineSet dataSet = new LineSet();
        dataSet.addPoints(lineLabels, lineValues);
        dataSet.setDots(true)
                .setDotsColor(this.getResources().getColor(android.R.color.white))//bg
                .setDotsRadius(Tools.fromDpToPx(5))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(this.getResources().getColor(android.R.color.white))
                .setLineColor(this.getResources().getColor(android.R.color.white))
                .setLineThickness(Tools.fromDpToPx(3))
                .beginAt(1).endAt(lineLabels.length - 1);
        mLineChart.addData(dataSet);

        /*dataSet = new LineSet();
        dataSet.addPoints(lineLabels, lineValues[1]);
        dataSet.setLineColor(this.getResources().getColor(android.R.color.white))
                .setLineThickness(Tools.fromDpToPx(3))
                .setSmooth(true)
                .setDashed(true);
        mLineChart.addData(dataSet);*/

        mLineChart.setBorderSpacing(Tools.fromDpToPx(4))
                .setGrid(LineChartView.GridType.HORIZONTAL, mLineGridPaint)
                .setXAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYAxis(false)
                .setYLabels(YController.LabelPosition.OUTSIDE)
                .setAxisBorderValues(LINE_MIN, LINE_MAX, 5)
                .setLabelsFormat(new DecimalFormat("##'u'")).show();
        //.show()
        ;

        mLineChart.animateSet(0, new DashAnimation());
    }

    private final OnEntryClickListener lineEntryListener = new OnEntryClickListener(){
        @Override
        public void onClick(int setIndex, int entryIndex, Rect rect) {

            /*if(mLineTooltip == null)
                showLineTooltip(setIndex, entryIndex, rect);
            else
                dismissLineTooltip(setIndex, entryIndex, rect);*/
        }
    };

    private final View.OnClickListener lineClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            /*if(mLineTooltip != null)
                dismissLineTooltip(-1, -1, null);*/
        }
    };



}
