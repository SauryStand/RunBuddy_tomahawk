
package org.runbuddy.github.mikephil.charting.formatter;

import org.runbuddy.github.mikephil.charting.components.AxisBase;
import org.runbuddy.github.mikephil.charting.data.Entry;
import org.runbuddy.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Predefined value-formatter that formats large numbers in a pretty way.
 * Outputs: 856 = 856; 1000 = 1k; 5821 = 5.8k; 10500 = 10k; 101800 = 102k;
 * 2000000 = 2m; 7800000 = 7.8m; 92150000 = 92m; 123200000 = 123m; 9999999 =
 * 10m; 1000000000 = 1b; Special thanks to Roman Gromov
 * (https://github.com/romangromov) for this piece of code.
 *
 * @author Philipp Jahoda
 * @author Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 */
public class LargeValueFormatter implements ValueFormatter, AxisValueFormatter {

    private static String[] SUFFIX = new String[]{
            "", "k", "m", "b", "t"
    };
    private static final int MAX_LENGTH = 5;
    private String mText = "";


    /**
     * FormattedStringCache for formatting and caching.
     */
    protected FormattedStringCache.PrimDouble mFormattedStringCache;

    public LargeValueFormatter() {
        mFormattedStringCache = new FormattedStringCache.PrimDouble(new DecimalFormat("###E00"));
    }

    /**
     * Creates a formatter that appends a specified text to the result string
     *
     * @param appendix a text that will be appended
     */
    public LargeValueFormatter(String appendix) {
        this();
        mText = appendix;
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return makePretty(value) + mText;
    }

    // AxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return makePretty(value) + mText;
    }

    /**
     * Set an appendix text to be added at the end of the formatted value.
     *
     * @param appendix
     */
    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    /**
     * Set custom suffix to be appended after the values.
     * Default suffix: ["", "k", "m", "b", "t"]
     *
     * @param suff new suffix
     */
    public void setSuffix(String[] suff) {
        SUFFIX = suff;
    }

    /**
     * Formats each number properly. Special thanks to Roman Gromov
     * (https://github.com/romangromov) for this piece of code.
     */
    private String makePretty(double number) {

        // TODO : Should be better way to do this.  Double isn't the best key...
        String r = mFormattedStringCache.getFormattedValue(number);

        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1);

        r = r.replaceAll("E[0-9][0-9]", SUFFIX[combined / 3]);

        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }

        return r;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
