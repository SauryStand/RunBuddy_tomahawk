/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2015, Enno Gottschalk <mrmaffen@googlemail.com>
 *
 *   Tomahawk is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Tomahawk is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Tomahawk. If not, see <http://www.gnu.org/licenses/>.
 */
package org.runbuddy.tomahawk.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import org.runbuddy.R;
import org.runbuddy.libtomahawk.utils.ViewUtils;


public class SimplePagerIndicator extends FrameLayout {

    private ViewPager.OnPageChangeListener mForwardOnPageChangeListener;

    private ValueAnimator mAnimator;

    private int mItemCount;

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mAnimator != null) {
                int stepSize = 10000 / (mItemCount - 1);
                mAnimator.setCurrentPlayTime(
                        (long) (position * stepSize + positionOffset * stepSize));
            }
            mForwardOnPageChangeListener
                    .onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            mForwardOnPageChangeListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mForwardOnPageChangeListener.onPageScrollStateChanged(state);
        }
    };

    public SimplePagerIndicator(Context context) {
        super(context);
    }

    public SimplePagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(final ViewPager viewPager) {
        removeAllViews();
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        mItemCount = viewPager.getAdapter().getCount();
        ViewUtils.afterViewGlobalLayout(new ViewUtils.ViewRunnable(this) {
            @Override
            public void run() {
                View tabIndicator = LayoutInflater.from(getContext())
                        .inflate(R.layout.simplepagertabs_tab_indicator,
                                SimplePagerIndicator.this, false);
                tabIndicator.getLayoutParams().width
                        = getLayedOutView().getWidth() / mItemCount;
                addView(tabIndicator);
                int xGoal = getLayedOutView().getWidth()
                        - getLayedOutView().getWidth() / mItemCount;
                mAnimator = ObjectAnimator.ofFloat(tabIndicator, "x", 0, xGoal);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.setDuration(10000);
            }
        });
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mForwardOnPageChangeListener = onPageChangeListener;
    }

}
