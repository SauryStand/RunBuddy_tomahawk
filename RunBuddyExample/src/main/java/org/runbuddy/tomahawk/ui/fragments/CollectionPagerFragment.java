/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2013, Enno Gottschalk <mrmaffen@googlemail.com>
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
package org.runbuddy.tomahawk.ui.fragments;

import android.os.Bundle;
import android.view.View;

import org.runbuddy.libtomahawk.collection.Collection;
import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.DbCollection;
import org.runbuddy.libtomahawk.collection.UserCollection;
import org.runbuddy.tomahawk.R;
import org.runbuddy.tomahawk.utils.FragmentInfo;

import java.util.ArrayList;
import java.util.List;

public class CollectionPagerFragment extends PagerFragment {

    /**
     * Called, when this {@link org.runbuddy.tomahawk.ui.fragments.CollectionPagerFragment}'s
     * {@link View} has been created
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().containsKey(TomahawkFragment.COLLECTION_ID)) {
            String collectionId = getArguments().getString(TomahawkFragment.COLLECTION_ID);
            Collection collection = CollectionManager.get().getCollection(collectionId);
            if (collection == null) {
                getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
            getActivity().setTitle(collection.getName());//设置title咯，没有layout，layout是后面自己画出来
            if (collection instanceof UserCollection) {
                showContentHeader(R.drawable.drawer_background);
            } else if (collection instanceof DbCollection) {
                showContentHeader(((DbCollection) collection).getIconBackgroundPath());
            }
        } else {
            throw new RuntimeException("No collection-id provided to CollectionPagerFragment");
        }

        int initialPage = -1;
        if (getArguments() != null) {
            if (getArguments().containsKey(TomahawkFragment.CONTAINER_FRAGMENT_PAGE)) {
                initialPage = getArguments().getInt(TomahawkFragment.CONTAINER_FRAGMENT_PAGE);
            }
        }

        List<FragmentInfoList> fragmentInfoLists = new ArrayList<>();
        FragmentInfoList fragmentInfoList = new FragmentInfoList();
        FragmentInfo fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = ArtistsFragment.class;
        fragmentInfo.mTitle = getString(R.string.artists);
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);

        fragmentInfoList = new FragmentInfoList();
        fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = AlbumsFragment.class;
        fragmentInfo.mTitle = getString(R.string.albums);
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);

        fragmentInfoList = new FragmentInfoList();
        fragmentInfo = new FragmentInfo();
        fragmentInfo.mClass = PlaylistEntriesFragment.class;
        fragmentInfo.mTitle = getString(R.string.tracks);//Track,也就是本地列表的歌曲,貌似不能扫描到外部sd卡
        fragmentInfo.mBundle = getChildFragmentBundle();
        fragmentInfoList.addFragmentInfo(fragmentInfo);
        fragmentInfoLists.add(fragmentInfoList);


        setupPager(fragmentInfoLists, initialPage, null, 2);//就一共3个子fragment啦
    }
}
