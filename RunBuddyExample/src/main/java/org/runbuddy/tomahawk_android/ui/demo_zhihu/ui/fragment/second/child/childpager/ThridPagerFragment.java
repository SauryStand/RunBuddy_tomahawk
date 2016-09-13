package org.runbuddy.tomahawk_android.ui.demo_zhihu.ui.fragment.second.child.childpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.ui.demo_zhihu.basic.BaseFragment;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ThridPagerFragment extends BaseFragment {

    private static final String ARG_TYPE = "arg_pos";

    public static ThridPagerFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        ThridPagerFragment fragment = new ThridPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_second_pager_thrid, container, false);
        //initView(view);
        return view;
    }


}
