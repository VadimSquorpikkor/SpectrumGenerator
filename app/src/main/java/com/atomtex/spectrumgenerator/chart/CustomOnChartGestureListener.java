package com.atomtex.spectrumgenerator.chart;

import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * This class contains callback methods of users interactions with the chart.
 *
 * <p>
 * This class allows to get callback from chart when user interact with it.
 *
 * @author stanislav.kleinikov@gmain.com
 */
public class CustomOnChartGestureListener implements OnChartGestureListener {

    /**
     * The instance of chart to get callback while user interact with it.
     */
    private LineChart mChart;

    public CustomOnChartGestureListener(LineChart chart) {
        mChart = chart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    /**
     * Fits chart to screen size on double tap
     *
     * @param me MotionEvent object
     */
    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        mChart.fitScreen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
