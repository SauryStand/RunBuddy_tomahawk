package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.runbuddy.tomahawk_android.demo_zhihu.basic.BaseLazyMainFragment;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuFirstFragment extends BaseLazyMainFragment {

    public static ZhihuFirstFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFirstFragment fragment = new ZhihuFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /*
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_first, container, false);
        return view;
    }*/

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            //loadRootFragment(R.id.fl_first_container, FirstHomeFragment.newInstance());
        }
    }
}
