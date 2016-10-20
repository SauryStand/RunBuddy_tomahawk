/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2016, Enno Gottschalk <mrmaffen@googlemail.com>
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
package org.runbuddy.tomahawk.listeners;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.runbuddy.tomahawk.ui.fragments.tai.homepage.HomePageFragment;
import org.runbuddy.tomahawk.ui.fragments.tai.mv.MVFragment;

import org.jdeferred.DoneCallback;
import org.runbuddy.libtomahawk.infosystem.User;
import org.runbuddy.tomahawk.app.TomahawkApp;
import org.runbuddy.tomahawk.activities.TomahawkMainActivity;
import org.runbuddy.tomahawk.adapters.TomahawkMenuAdapter;
import org.runbuddy.tomahawk.ui.fragments.ChartsSelectorFragment;
import org.runbuddy.tomahawk.ui.fragments.CollectionPagerFragment;
import org.runbuddy.tomahawk.ui.fragments.ContentHeaderFragment;
import org.runbuddy.tomahawk.ui.fragments.PlaylistEntriesFragment;
import org.runbuddy.tomahawk.ui.fragments.PlaylistsFragment;
import org.runbuddy.tomahawk.ui.fragments.PreferencePagerFragment;
import org.runbuddy.tomahawk.ui.fragments.SensorFragment;
import org.runbuddy.tomahawk.ui.fragments.TomahawkFragment;
import org.runbuddy.tomahawk.ui.fragments.UserPagerFragment;
import org.runbuddy.tomahawk.ui.fragments.star_page.StarPageFragment;
import org.runbuddy.tomahawk.utils.FragmentUtils;
import org.runbuddy.tomahawk.utils.MenuDrawer;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MenuDrawerListener implements ListView.OnItemClickListener {
    private TomahawkMainActivity mActivity;
    private StickyListHeadersListView mDrawerList;

    private MenuDrawer mMenuDrawer;

    public MenuDrawerListener(TomahawkMainActivity activity, StickyListHeadersListView drawerList,
                              MenuDrawer menuDrawer) {
        mActivity = activity;
        mDrawerList = drawerList;
        mMenuDrawer = menuDrawer;
    }

    /**
     * Called every time an item inside the {@link ListView} is clicked
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this will be a view
     *                 provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TomahawkMenuAdapter.ResourceHolder holder =
                (TomahawkMenuAdapter.ResourceHolder) mDrawerList.getAdapter().getItem(position);
        final Bundle bundle = new Bundle();
        if (holder.collection != null) {
            bundle.putString(TomahawkFragment.COLLECTION_ID, holder.collection.getId());
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC);
            FragmentUtils.replace(mActivity, CollectionPagerFragment.class, bundle);
        } else if (holder.id.equals(MenuDrawer.HUB_ID_USERPAGE)) {
            User.getSelf().done(new DoneCallback<User>() {
                @Override
                public void onDone(User user) {
                    bundle.putString(TomahawkFragment.USER, user.getId());
                    bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            ContentHeaderFragment.MODE_HEADER_STATIC_USER);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replace(mActivity, UserPagerFragment.class, bundle);
                        }
                    });
                }
            });
        } else if (holder.id.equals(MenuDrawer.HUB_ID_FEED)) {
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_ACTIONBAR_FILLED);
            FragmentUtils.replace(mActivity, StarPageFragment.class, bundle);//测试用，临时改了
            /*不需要传用户信息
            User.getSelf().done(new DoneCallback<User>() {
                @Override
                public void onDone(User user) {
                    bundle.putString(TomahawkFragment.USER, user.getId());
                    bundle.putInt(TomahawkFragment.SHOW_MODE,
                            SocialActionsFragment.SHOW_MODE_DASHBOARD);
                    bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            ContentHeaderFragment.MODE_ACTIONBAR_FILLED);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replace(mActivity, SocialActionsFragment.class, bundle);
                        }
                    });
                }
            });*/

        } else if (holder.id.equals(MenuDrawer.HUB_ID_CHARTS)) {
            FragmentUtils
                    .replace(mActivity, ChartsSelectorFragment.class, bundle);
        } else if (holder.id.equals(MenuDrawer.HUB_ID_COLLECTION)) {
            bundle.putString(TomahawkFragment.COLLECTION_ID,
                    TomahawkApp.PLUGINNAME_USERCOLLECTION);
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC);
            FragmentUtils.replace(mActivity, CollectionPagerFragment.class, bundle);
        } else if (holder.id.equals(MenuDrawer.HUB_ID_LOVEDTRACKS)) {
            User.getSelf().done(new DoneCallback<User>() {
                @Override
                public void onDone(User user) {
                    bundle.putInt(TomahawkFragment.SHOW_MODE,
                            PlaylistEntriesFragment.SHOW_MODE_LOVEDITEMS);
                    bundle.putString(TomahawkFragment.USER, user.getId());
                    bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            ContentHeaderFragment.MODE_HEADER_DYNAMIC);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replace(mActivity, PlaylistEntriesFragment.class, bundle);
                        }
                    });
                }
            });
        } else if (holder.id.equals(MenuDrawer.HUB_ID_PLAYLISTS)) {
            User.getSelf().done(new DoneCallback<User>() {
                @Override
                public void onDone(User user) {
                    bundle.putString(TomahawkFragment.USER, user.getId());
                    bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            ContentHeaderFragment.MODE_HEADER_STATIC);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replace(mActivity, PlaylistsFragment.class, bundle);
                        }
                    });
                }
            });
        } else if (holder.id.equals(MenuDrawer.HUB_ID_STATIONS)) {
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC);
            FragmentUtils.replace(mActivity, SensorFragment.class, bundle);
            /*
            User.getSelf().done(new DoneCallback<User>() {
                @Override
                public void onDone(User user) {
                    bundle.putString(TomahawkFragment.USER, user.getId());
                    bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            ContentHeaderFragment.MODE_HEADER_STATIC);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replace(mActivity, StationsFragment.class, bundle);
                        }
                    });
                }
            });*/
        } else if (holder.id.equals(MenuDrawer.HUB_ID_SETTINGS)) {
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC_SMALL);
            FragmentUtils.replace(mActivity, PreferencePagerFragment.class, bundle);
        } else if (holder.id.equals(MenuDrawer.HUB_ID_MYSETTING)) {
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC_SMALL);//flag
            FragmentUtils.replace(mActivity, MVFragment.class, bundle);
        } else if (holder.id.equals(MenuDrawer.HUB_ID_POPULARPAGE)) {
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_STATIC_SMALL);//2016.08.07
            FragmentUtils.replace(mActivity, HomePageFragment.class, bundle);//测试用，临时改了
        }

        if (mMenuDrawer != null) {
            mMenuDrawer.closeDrawer();
        }
    }

}
