package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import org.tomahawk.tomahawk_android.R;

/**
 * Created by Johnny Chow on 2016/8/7.
 */
public class PopularPageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_popularpage);
        View view = inflater.inflate(R.layout.item_trainning_detail, container, false);
        return view;
    }
}
