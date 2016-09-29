package org.runbuddy.tomahawk_android.ui.fragments.tai.yuedan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.listeners.PlayVideoListener;
import org.runbuddy.tomahawk_android.model.domain.YueDanDetailBean;
import org.runbuddy.tomahawk_android.adapters.tai.YueDanDetailRecycleViewAdapter;
import org.runbuddy.tomahawk_android.widgets.tai.RecycleViewDivider;

import org.tomahawk.tomahawk_android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/23
 * YinYueTai
 */
public class YueDanListFragment extends Fragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private View rootView;
    private boolean hasLoadOnce;

    public static YueDanListFragment newInstance(YueDanDetailBean yueDanDetailBean) {
        YueDanListFragment fragment = new YueDanListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("yueDanDetailBean", yueDanDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }
    private PlayVideoListener playVideoListener;

    public void setPlayVideoListener(PlayVideoListener playVideoListener) {
        this.playVideoListener = playVideoListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.recycleview_layout, container, false);
        }
        ButterKnife.bind(this, rootView);
        if (!hasLoadOnce){
            hasLoadOnce = true;
            initView();
        }
        return rootView;
    }
    private void initView(){
        YueDanDetailBean yueDanDetailBean = getArguments().getParcelable("yueDanDetailBean");
        YueDanDetailRecycleViewAdapter adapter = new YueDanDetailRecycleViewAdapter(getActivity(),yueDanDetailBean.getVideos(),playVideoListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
