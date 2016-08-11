package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentation.Fragmentation;
import com.fragmentation.SupportFragment;

import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.first.ZhihuFirstFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.fourth.ZhihuFourthFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second.ZhihuSecondFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.third.ZhihuThirdFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.view.BottomBar;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.view.BottomBarTab;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by Johnny Chow on 2016/8/7.
 */
public class PopularPageFragment extends Fragment {
    private Fragmentation mFragmentation;
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private BottomBar mBottomBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_popularpage);
        View view = inflater.inflate(R.layout.zhihu_activity_main, container, false);

        /*
        if(savedInstanceState == null){
            mFragments[FIRST] = ZhihuFirstFragment.newInstance();
            mFragments[SECOND] = ZhihuSecondFragment.newInstance();
            mFragments[THIRD] = ZhihuThirdFragment.newInstance();
            mFragments[FOURTH] = ZhihuFourthFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        }else{
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findFragment(ZhihuFirstFragment.class);
            mFragments[SECOND] = findFragment(ZhihuSecondFragment.class);
            mFragments[THIRD] = findFragment(ZhihuThirdFragment.class);
            mFragments[FOURTH] = findFragment(ZhihuFourthFragment.class);
        }*/

        initView(view);

        return view;
    }



    private void initView(View view) {

        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(view.getContext(), R.drawable.ic_home_white_24dp))
                .addItem(new BottomBarTab(view.getContext(), R.drawable.ic_discover_white_24dp))
                .addItem(new BottomBarTab(view.getContext(), R.drawable.ic_message_white_24dp))
                .addItem(new BottomBarTab(view.getContext(), R.drawable.ic_account_circle_white_24dp));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                SupportFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
                //Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                // 如果不在该类别Fragment的主页,则回到主页;
                if (count > 1) {
                    if (currentFragment instanceof ZhihuFirstFragment) {
                        //currentFragment.popToChild(FirstHomeFragment.class, false);
                    } else if (currentFragment instanceof ZhihuSecondFragment) {
                        //currentFragment.popToChild(ViewPagerFragment.class, false);
                    } else if (currentFragment instanceof ZhihuThirdFragment) {
                        //currentFragment.popToChild(ShopFragment.class, false);
                    } else if (currentFragment instanceof ZhihuFourthFragment) {
                        //currentFragment.popToChild(MeFragment.class, false);
                    }
                    return;
                }

            }
        });

    }


}
