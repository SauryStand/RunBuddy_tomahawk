package org.runbuddy.tomahawk_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.utils.FragmentInfo;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class LocalMusicListFragment extends Fragment {

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




}
