package com.atomtex.spectrumgenerator.chart;

import com.github.mikephil.charting.data.BarEntry;

/**
 * This class extends {@link BarEntry} and adds the color to its properties
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class CustomBarEntry extends BarEntry {

    /**
     * The color to draw bar.
     *
     * @see android.graphics.Color
     */
    private int mColor;

    public CustomBarEntry(float x, float y) {
        super(x, y);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
