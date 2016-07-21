/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2014, Enno Gottschalk <mrmaffen@googlemail.com>
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
package org.runbuddy.tomahawk_android.fragments;


import android.os.Bundle;
import android.view.View;

import org.runbuddy.tomahawk_android.utils.FragmentInfo;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

public class PreferencePagerFragment extends PagerFragment {

    /**
     * Called, when this {@link org.runbuddy.tomahawk_android.fragments.PreferencePagerFragment}'s
     * {@link android.view.View} has been created
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.drawer_title_settings).toUpperCase());

        int initialPage = -1;
        if (getArguments() != null) {
            if (getArguments().containsKey(TomahawkFragment.CONTAINER_FRAGMENT_PAGE)) {
                initialPage = getArguments().getInt(TomahawkFragment.CONTAINER_FRAGMENT_PAGE);
            }
        }

        showContentHeader(R.drawable.settings_header);

        List<FragmentInfoList> fragmentInfoLists = new ArrayList<>();
        FragmentInfoList fragmentInfoList = new FragmentInfoList();
        FragmentInfo fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = PreferenceConnectFragment.class;
        fragmentInfo.mTitle = getString(R.string.connect);
        fragmentInfo.mIconResId = R.drawable.ic_connect;
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);

        fragmentInfoList = new FragmentInfoList();
        fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = PreferenceAdvancedFragment.class;
        fragmentInfo.mTitle = getString(R.string.advanced);
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);

        fragmentInfoList = new FragmentInfoList();
        fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = PreferenceInfoFragment.class;
        fragmentInfo.mTitle = getString(R.string.info);
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);

        setupPager(fragmentInfoLists, initialPage, null, 1);
    }
}
