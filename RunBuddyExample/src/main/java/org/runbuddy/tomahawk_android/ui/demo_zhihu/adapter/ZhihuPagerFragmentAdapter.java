package org.runbuddy.tomahawk_android.ui.demo_zhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.runbuddy.tomahawk_android.ui.demo_zhihu.ui.fragment.second.child.childpager.FirstPagerFragment;
import org.runbuddy.tomahawk_android.ui.demo_zhihu.ui.fragment.second.child.childpager.SecondPagerFragment;
import org.runbuddy.tomahawk_android.ui.demo_zhihu.ui.fragment.second.child.childpager.ThridPagerFragment;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class ZhihuPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"推荐", "发现", "身边"};

    public ZhihuPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagerFragment.newInstance();
        }else if(position == 1){
            return SecondPagerFragment.newInstance(position);
        }
        else {
            return ThridPagerFragment.newInstance(position);
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
