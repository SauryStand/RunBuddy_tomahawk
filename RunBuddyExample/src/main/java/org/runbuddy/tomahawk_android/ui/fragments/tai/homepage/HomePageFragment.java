package org.runbuddy.tomahawk_android.ui.fragments.tai.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import org.runbuddy.tomahawk_android.model.domain.VideoBean;
import org.runbuddy.tomahawk_android.presenter.HomePageFragmentContract;
import org.runbuddy.tomahawk_android.presenter.HomePagePresenter;
import org.runbuddy.tomahawk_android.adapters.tai.FirstRecycleViewAdapter;
import org.runbuddy.tomahawk_android.ui.fragments.tai.BaseFragment;

import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/10
 * YinYueTai
 */
public class HomePageFragment extends BaseFragment implements HomePageFragmentContract.View {
    @Bind(R.id.first_page_recyclerView)
    RecyclerView firstPageRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private FirstRecycleViewAdapter recycleViewAdapter;
    private ArrayList<VideoBean> firstPageBeanList;
    private MaterialDialog.Builder builder;
    private MaterialDialog materialDialog;

    private HomePageFragmentContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.first_page_fragment, container, false);
            ButterKnife.bind(this, rootView);
            firstPageBeanList = new ArrayList<>();
            observerView(540,640);
            new HomePagePresenter(this);
            initView();
            presenter.getData(mOffset, SIZE);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initView() {
        recycleViewAdapter = new FirstRecycleViewAdapter(firstPageBeanList, getActivity(), mWidth, mHeight);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        firstPageRecyclerView.setLayoutManager(linearLayoutManager);
        firstPageRecyclerView.setAdapter(recycleViewAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                presenter.getData(0, SIZE);
            }
        });
        showLoading();
        firstPageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getActivity()).resumeRequests();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1 == recycleViewAdapter.getItemCount()) && hasMore) {
                    swipeRefreshLayout.setRefreshing(true);
                    presenter.getData(mOffset, SIZE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                Glide.with(getActivity()).pauseRequests();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstPageRecyclerView.scrollToPosition(0);
            }
        });
    }

    private void showLoading() {
        if (builder == null) {
            builder = new MaterialDialog.Builder(getActivity());
            builder.cancelable(false);
            builder.title("等一下");
            builder.content("正在努力加载...")
                    .progress(true, 0);
        }
        materialDialog = builder.show();
    }

    private void dismissLoading() {
        materialDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void setData(ArrayList<VideoBean> dataList) {
        if (refresh){
            refresh = false;
            firstPageBeanList.clear();
            mOffset = 0;
        }
        if (dataList.size() > 0){
            hasMore = true;
        }else {
            hasMore = false;
        }
        mOffset += dataList.size();
        firstPageBeanList.addAll(dataList);
        recycleViewAdapter.notifyDataSetChanged();
        dismissLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        swipeRefreshLayout.setRefreshing(false);
        if (refresh){
            refresh = false;
            Toast.makeText(getActivity(),"刷新失败",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
        }
        dismissLoading();
    }

    @Override
    public void setPresenter(HomePageFragmentContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
