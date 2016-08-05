package org.runbuddy.tomahawk_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.utils.FragmentInfo;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class LocalMusicListFragment extends PagerFragment {

    public static final int MODE_HEADER_STATIC_SMALL = 8;

    private MenuItem mCountryCodePicker;

    private List<FragmentInfo> mFragmentInfos = new ArrayList<>();

    private FragmentInfo mSelectedFragmentInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //showContentHeader(R.drawable.drawer_background);
        getActivity().setTitle(R.string.drawer_title_localList);
        return inflater.inflate(R.layout.item_trainning_detail_listview_home, container, false);
    }
    /*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showContentHeader(R.drawable.drawer_background);
        getActivity().setTitle(R.string.drawer_title_localList);

        int initialPage = -1;
        //跟这个if语句后面的方法有关;
        if (getArguments() != null) {
            if (getArguments().containsKey(TomahawkFragment.CONTAINER_FRAGMENT_PAGE)) {
                initialPage = getArguments().getInt(TomahawkFragment.CONTAINER_FRAGMENT_PAGE);
            }
        }
    }
    */

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }




}
