package org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second.child.childpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentation.SupportFragment;

import org.greenrobot.eventbus.EventBus;
import org.runbuddy.tomahawk_android.demo_zhihu.adapter.SecondHomeAdapter;
import org.runbuddy.tomahawk_android.demo_zhihu.basic.BaseFragment;
import org.runbuddy.tomahawk_android.demo_zhihu.entity.Article;
import org.runbuddy.tomahawk_android.demo_zhihu.listener.OnItemClickListener;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.fragment.second.child.DetailFragment;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class SecondPagerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static final String ARG_TYPE = "arg_pos";
    public static int TYPE_HOT = 1;
    private int mType = TYPE_HOT;
    private TextView mTvTitle;
    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;
    private SecondHomeAdapter mAdapter;
    private boolean mAtTop = true;
    private int mScrollTotal;

    private String[] mTitles = new String[]{
            "2航拍“摩托大军”返乡高峰 如蚂蚁搬家（组图）",
            "2苹果因漏电召回部分电源插头",
            "2IS宣称对叙利亚爆炸案负责"
    };

    private String[] mContents = new String[]{
            "1月30日，距离春节还有不到十天，“摩托大军”返乡高峰到来。航拍广西梧州市东出口服务站附近的骑行返乡人员，如同蚂蚁搬家一般。",
            "昨天记者了解到，苹果公司在其官网发出交流电源插头转换器更换计划，召回部分可能存在漏电风险的电源插头。",
            "极端组织“伊斯兰国”31日在社交媒体上宣称，该组织制造了当天在叙利亚首都大马士革发生的连环爆炸案。"
    };


    public static SecondPagerFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        SecondPagerFragment fragment = new SecondPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(ARG_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_second_pager_second, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new SecondHomeAdapter(_mActivity);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 这里的DetailFragment在flow包里
                // 这里是父Fragment启动,要注意 栈层级
                ((SupportFragment) getParentFragment()).start(DetailFragment.newInstance(mAdapter.getItem(position).getTitle()));
                Toast.makeText(getContext(),"seek for help",Toast.LENGTH_SHORT).show();

                //DetailFragment.newInstance(mAdapter.getItem(position).getTitle());
            }
        });

        // Init Datas
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int index = (int) (Math.random() * 3);
            Article article = new Article(mTitles[index], mContents[index]);
            articleList.add(article);
        }
        mAdapter.setDatas(articleList);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mAtTop = true;
                } else {
                    mAtTop = false;
                }
            }
        });
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

}
