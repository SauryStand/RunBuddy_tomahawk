package org.runbuddy.tomahawk_android.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.runbuddy.tomahawk_android.fragments.Category_page.constant.Apis;
import org.runbuddy.tomahawk_android.fragments.Category_page.presenter.ICategoryPresenter;
import org.runbuddy.tomahawk_android.fragments.Category_page.presenter.impl.CategoryPresenterImpl;
import org.runbuddy.tomahawk_android.fragments.Category_page.ui.adapter.GanHuoPagerAdapter;
import org.runbuddy.tomahawk_android.fragments.Category_page.ui.view.ICategoryView;
import org.tomahawk.tomahawk_android.R;

import java.util.List;

import ren.solid.library.fragment.base.BaseFragment;
import ren.solid.library.utils.Logger;

/**
 * Created by Jnhnny Chow on 2016/7/26.
 *
 */
public class MomentsFragment extends BaseFragment implements ICategoryView{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private GanHuoPagerAdapter mAdapter;
    private ICategoryPresenter mIHomePresenter;


    /*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_running_page);
        View view = inflater.inflate(R.layout.moments_fragments, container, false);
        initView(view);
        return view;
    }*/

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_category;
    }

    @Override
    protected void handleRxMsg(String msg) {
        Logger.i(this, msg);
        if (msg.equals("SortChange")) {
            mAdapter.addAll(Apis.getGanHuoCateGory());
        }
    }


    @Override
    protected void setUpView() {
        mTabLayout = $(R.id.tab_layout);
        mViewPager = $(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(5);
    }

    @Override
    protected void setUpData() {
        mIHomePresenter = new CategoryPresenterImpl(this);
        //减少打开Fragment时切换时的一些卡顿
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIHomePresenter.getAdapterData();
            }
        }, 200);
    }


    @Override
    public void setPagerAdapterData(List<String> list) {
        mAdapter = new GanHuoPagerAdapter(getChildFragmentManager(), list);
        for (int i = 0; i < list.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(list.get(i)));
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errMsg) {

    }
}
