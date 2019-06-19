package com.atomtex.spectrumgenerator.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * This class was created to change usual behavior of {@link LineChartRenderer}.
 * <p>
 * The class contains value {{@link #mDottedMode}}  and doesn't draw a line in case of value is <code>true</code>
 * It's needed to display line chart consisting off dots without line connecting them.
 */
public class CustomLineChartRenderer extends LineChartRenderer {

    /**
     * Indicates whether the "dotted" mode is active.
     *
     * <p>
     * <code>true</code> if the dotted mode is active or <code>false</code> otherwise.
     */
    private boolean mDottedMode;

    public CustomLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    public boolean isDottedMode() {
        return mDottedMode;
    }

    public void setDottedMode(boolean dottedMode) {
        mDottedMode = dottedMode;
    }

    /**
     * Draws data set on the chart according to one of the mode.
     *
     * @see com.github.mikephil.charting.data.LineDataSet.Mode
     */
    @Override
    protected void drawDataSet(Canvas c, ILineDataSet dataSet) {
        if (dataSet.getEntryCount() < 1)
            return;

        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setPathEffect(dataSet.getDashPathEffect());

        switch (dataSet.getMode()) {
            default:
            case LINEAR:
            case STEPPED:
                //Changing the behavior
                //This code allows to draws a data set as a plenty of dots if mDottedMode is true
                //or use a standard behavior otherwise
                if (!mDottedMode) {
                    drawLinear(c, dataSet);
                }
                break;

            case CUBIC_BEZIER:
                drawCubicBezier(dataSet);
                break;

            case HORIZONTAL_BEZIER:
                drawHorizontalBezier(dataSet);
                break;
        }

        mRenderPaint.setPathEffect(null);
    }
}
