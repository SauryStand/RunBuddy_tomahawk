package org.runbuddy.tomahawk.ui.fragments.starpage.widgets;

/**
 * Created by Johnny Chow on 2016/8/21.
 */
public class ViewModel {
    private String text;
    private String image;

    public ViewModel(String mtext, String mimage) {
        this.text = mtext;
        this.image = mimage;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}
