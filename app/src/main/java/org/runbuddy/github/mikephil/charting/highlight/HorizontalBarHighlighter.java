package org.runbuddy.github.mikephil.charting.highlight;

import org.runbuddy.github.mikephil.charting.data.BarData;
import org.runbuddy.github.mikephil.charting.data.DataSet;
import org.runbuddy.github.mikephil.charting.data.Entry;
import org.runbuddy.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import org.runbuddy.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import org.runbuddy.github.mikephil.charting.interfaces.datasets.IDataSet;
import org.runbuddy.github.mikephil.charting.utils.MPPointD;

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
public class HorizontalBarHighlighter extends BarHighlighter {

	public HorizontalBarHighlighter(BarDataProvider chart) {
		super(chart);
	}

	@Override
	public Highlight getHighlight(float x, float y) {

		BarData barData = mChart.getBarData();

		MPPointD pos = getValsForTouch(y, x);

		Highlight high = getHighlightForX((float) pos.y, y, x);
		if (high == null)
			return null;

		IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
		if (set.isStacked()) {

			return getStackedHighlight(high,
					set,
					(float) pos.y,
					(float) pos.x);
		}

		MPPointD.recycleInstance(pos);

		return high;
	}

	@Override
	protected Highlight buildHighlight(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {

		final Entry e = set.getEntryForXPos(xVal, rounding);

		MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelsForValues(e.getY(), e.getX());

		return new Highlight(e.getX(), e.getY(), (float) pixels.x, (float) pixels.y, dataSetIndex, set.getAxisDependency());
	}

	@Override
	protected float getDistance(float x1, float y1, float x2, float y2) {
		return Math.abs(y1 - y2);
	}
}
