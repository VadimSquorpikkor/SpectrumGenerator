package com.atomtex.spectrumgenerator.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * This class was designed to change standard behavior of {@link XAxisRenderer}
 * <p>
 * Allows to render limit lines one under another with the offset adding a background to the
 * markers label.
 */
public class CustomXAxisRenderer extends XAxisRenderer {

    /**
     * The buffer for positions of a limit line on the chart.
     *
     * <p>
     * mLimitLineSegmentsBuffer[0] - left position
     * mLimitLineSegmentsBuffer[1] - top position
     * mLimitLineSegmentsBuffer[2] - right position
     * mLimitLineSegmentsBuffer[3] - bottom position
     */
    private float[] mLimitLineSegmentsBuffer = new float[4];

    /**
     * This {@link Path} is used to draw a limit line
     * <p>
     * The Path class encapsulates compound (multiple contour) geometric paths
     * consisting of straight line segments, quadratic curves, and cubic curves.
     * It can be drawn with canvas.drawPath(path, paint), either filled or stroked
     * (based on the paint's Style), or it can be used for clipping or to draw
     * text on a path.
     */
    private Path mLimitLinePath = new Path();

    /**
     * Offset for limit line from the top of the chart. Allows to draw limit line one under another
     * to prevent overlap.
     */
    private float mYOffset;

    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    /**
     * Draws the LimitLines and their labels associated with this axis to the screen.
     */
    @Override
    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = mXAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] position = mRenderLimitLinesBuffer;
        position[0] = 0;
        position[1] = 0;

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled()) {
                continue;
            }

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(-l.getLineWidth(), 0.f);
            c.clipRect(mLimitLineClippingRect);

            position[0] = l.getLimit();
            position[1] = i;

            //Count offset to draw limitLines and their labels one under another
            mYOffset = (0.4f + mYOffset) * i;

            mTrans.pointValuesToPixel(position);

            renderLimitLineLine(c, l, position);
            renderLimitLineLabel(c, l, position, mYOffset);

            c.restoreToCount(clipRestoreCount);
        }
    }

    /**
     * Draws limitLine.
     */
    @Override
    public void renderLimitLineLine(Canvas c, LimitLine limitLine, float[] position) {
        mLimitLineSegmentsBuffer[0] = position[0];
        mLimitLineSegmentsBuffer[1] = mViewPortHandler.contentTop() + mYOffset;
        mLimitLineSegmentsBuffer[2] = position[0];
        mLimitLineSegmentsBuffer[3] = mViewPortHandler.contentBottom();

        mLimitLinePath.reset();
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1]);
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3]);

        mLimitLinePaint.setStyle(Paint.Style.STROKE);
        mLimitLinePaint.setColor(limitLine.getLineColor());
        mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());

        c.drawPath(mLimitLinePath, mLimitLinePaint);
    }

    /**
     * Draws limitLine's label.
     */
    @Override
    public void renderLimitLineLabel(Canvas c, LimitLine limitLine, float[] position, float yOffset) {
        String label = limitLine.getLabel();

        // if drawing the limit-value label is enabled
        if (label != null && !label.equals("")) {

            mLimitLinePaint.setStyle(limitLine.getTextStyle());
            mLimitLinePaint.setPathEffect(null);
            mLimitLinePaint.setColor(limitLine.getTextColor());
            mLimitLinePaint.setStrokeWidth(0.5f);
            mLimitLinePaint.setTextSize(limitLine.getTextSize());

            float xOffset = limitLine.getLineWidth();

            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();

            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {

                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                mYOffset = labelLineHeight;
                final float labelWidth = Utils.calcTextWidth(mLimitLinePaint, label);
                mLimitLinePaint.setTextAlign(Paint.Align.LEFT);

                float left = position[0] + xOffset;
                float top = mViewPortHandler.contentTop() + yOffset;
                float right = position[0] + xOffset + labelWidth;
                float bottom = mViewPortHandler.contentTop() + yOffset + labelLineHeight;
                float textLeft = position[0] + xOffset;

                if (right >= mViewPortHandler.contentRight()) {
                    right = position[0] - xOffset;
                    left = position[0] - xOffset - labelWidth;
                    textLeft = position[0] - xOffset;
                    mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                }

                mLimitLinePaint.setColor(Color.BLACK);
                mLimitLinePaint.setStyle(Paint.Style.FILL);
                c.drawRect(left, top,
                        right, bottom, mLimitLinePaint);
                mLimitLinePaint.setColor(limitLine.getTextColor());

                c.drawText(label, textLeft, mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint);

            } else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                c.drawText(label, position[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint);
            } else {
                mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            }
        }
    }
}
