package org.runbuddy.github.mikephil.charting.interfaces.dataprovider;

import org.runbuddy.github.mikephil.charting.components.YAxis.AxisDependency;
import org.runbuddy.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import org.runbuddy.github.mikephil.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
