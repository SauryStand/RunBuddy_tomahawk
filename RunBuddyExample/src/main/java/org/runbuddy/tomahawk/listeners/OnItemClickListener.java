package org.runbuddy.tomahawk.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jonney Chou on 2016/7/30.
 */
public interface OnItemClickListener {
    void onItemClickListener(int position, View view, RecyclerView.ViewHolder vh);
}
