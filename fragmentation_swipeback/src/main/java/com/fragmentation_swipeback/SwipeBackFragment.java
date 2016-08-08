package com.fragmentation_swipeback;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentation.SupportFragment;

import com.fragmentation.SwipeBackLayout;



/**
 * SwipeBackFragment
 * Created by YoKeyword on 16/4/19.
 */
public class SwipeBackFragment extends SupportFragment {
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onFragmentCreate();
    }

    private void onFragmentCreate() {
        mSwipeBackLayout = new SwipeBackLayout(_mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout.setLayoutParams(params);
        mSwipeBackLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    protected View attachToSwipeBack(View view) {
        mSwipeBackLayout.attachToFragment(this,view);
        return mSwipeBackLayout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mSwipeBackLayout != null) {
            mSwipeBackLayout.hiddenFragment();
        }
    }

    @Override
    protected void initFragmentBackground(View view) {
        if (!(view instanceof SwipeBackLayout) && view != null && view.getBackground() == null) {
            int background = getWindowBackground();
            view.setBackgroundResource(background);
        } else {
            if (view instanceof SwipeBackLayout) {
                View childView = ((SwipeBackLayout) view).getChildAt(0);
                if (childView != null && childView.getBackground() == null) {
                    int background = getWindowBackground();
                    childView.setBackgroundResource(background);
                }
            }
        }
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnableGesture(enable);
    }
}
