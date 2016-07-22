package org.runbuddy.github.mikephil.charting.interfaces.dataprovider;

import org.runbuddy.github.mikephil.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
