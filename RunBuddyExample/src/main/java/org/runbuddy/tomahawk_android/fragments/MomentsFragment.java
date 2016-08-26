package org.runbuddy.tomahawk_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jnhnny Chow on 2016/7/26.
 *
 */
public class MomentsFragment extends PagerFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_running_page);
        View view = inflater.inflate(R.layout.moments_fragments, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {







    }


}
