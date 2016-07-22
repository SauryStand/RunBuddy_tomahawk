
package org.runbuddy.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import org.runbuddy.github.mikephil.charting.data.CandleData;
import org.runbuddy.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import org.runbuddy.github.mikephil.charting.renderer.CandleStickChartRenderer;

/**
 * Financial chart type that draws candle-sticks (OHCL chart).
 *
 * @author Philipp Jahoda
 */
public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider {

    public CandleStickChart(Context context) {
        super(context);
    }

    public CandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new CandleStickChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    public CandleData getCandleData() {
        return mData;
    }
}
