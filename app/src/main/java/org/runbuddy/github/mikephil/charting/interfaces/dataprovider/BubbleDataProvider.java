package org.runbuddy.github.mikephil.charting.interfaces.dataprovider;

import org.runbuddy.github.mikephil.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
