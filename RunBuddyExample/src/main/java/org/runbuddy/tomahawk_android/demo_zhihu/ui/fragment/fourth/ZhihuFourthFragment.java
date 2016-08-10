package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.demo_zhihu.basic.BaseLazyMainFragment;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jonney Chou on 2016/8/8.
 */
public class ZhihuFourthFragment extends BaseLazyMainFragment {

    private Toolbar mToolbar;
    private View mView;

    public static ZhihuFourthFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFourthFragment fragment = new ZhihuFourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.zhihu_fragment_fourth, container, false);
        return mView;
    }


    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            //loadFragment();
        } else {  // 这里可能会出现该Fragment没被初始化时,就被强杀导致的没有load子Fragment
//            if (findChildFragment(AvatarFragment.class) == null) {
//                loadFragment();
//            }
        }

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        mToolbar.setTitle("我的");
        initToolbarMenu(mToolbar);
    }








}
