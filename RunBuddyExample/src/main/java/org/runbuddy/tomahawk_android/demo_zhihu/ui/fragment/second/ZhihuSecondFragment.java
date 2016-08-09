package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.demo_zhihu.basic.BaseLazyMainFragment;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuSecondFragment extends BaseLazyMainFragment {


    public static ZhihuSecondFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuSecondFragment fragment = new ZhihuSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_second, container, false);
        //initView(savedInstanceState);
        return view;
    }




    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }

}
