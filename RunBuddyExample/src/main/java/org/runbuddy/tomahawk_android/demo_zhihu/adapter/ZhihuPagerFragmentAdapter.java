package org.runbuddy.tomahawk_android.demo_zhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second.child.childpager.FirstPagerFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second.child.childpager.OtherPagerFragment;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class ZhihuPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"推荐", "发现", "收藏"};

    public ZhihuPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagerFragment.newInstance();
        } else {
            return OtherPagerFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
