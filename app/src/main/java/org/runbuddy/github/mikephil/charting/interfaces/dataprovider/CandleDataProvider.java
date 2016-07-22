package org.runbuddy.github.mikephil.charting.interfaces.dataprovider;

import org.runbuddy.github.mikephil.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
