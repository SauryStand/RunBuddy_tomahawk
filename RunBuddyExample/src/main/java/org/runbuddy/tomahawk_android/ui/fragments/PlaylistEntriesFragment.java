/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2012, Enno Gottschalk <mrmaffen@googlemail.com>
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
package org.runbuddy.tomahawk_android.ui.fragments;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;

import org.jdeferred.DoneCallback;
import org.runbuddy.libtomahawk.collection.Artist;
import org.runbuddy.libtomahawk.collection.Collection;
import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.Playlist;
import org.runbuddy.libtomahawk.collection.PlaylistEntry;
import org.runbuddy.libtomahawk.collection.ScriptResolverCollection;
import org.runbuddy.libtomahawk.database.DatabaseHelper;
import org.runbuddy.libtomahawk.infosystem.InfoSystem;
import org.runbuddy.libtomahawk.infosystem.User;
import org.runbuddy.tomahawk_android.adapters.Segment;
import org.runbuddy.tomahawk_android.utils.ThreadManager;
import org.runbuddy.tomahawk_android.utils.TomahawkRunnable;
import org.runbuddy.tomahawk_android.views.FancyDropDown;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link org.runbuddy.tomahawk_android.ui.fragments.TomahawkFragment} which shows a set of {@link
 * org.runbuddy.libtomahawk.collection.Track}s inside its {@link se.emilsjolander.stickylistheaders.StickyListHeadersListView}
 */
public class PlaylistEntriesFragment extends TomahawkFragment {

    private static final String TAG = PlaylistEntriesFragment.class.getSimpleName();

    public static final int SHOW_MODE_LOVEDITEMS = 0;

    public static final int SHOW_MODE_PLAYBACKLOG = 1;

    public static final String COLLECTION_TRACKS_SPINNER_POSITION
            = "org.tomahawk.tomahawk_android.collection_tracks_spinner_position_";

    private Set<String> mResolvingTopArtistNames = new HashSet<>();

    private Playlist mCurrentPlaylist;

    private String TAG_PLAYLIST = "PLAYLIST";//貌似没有排场用场

    @SuppressWarnings("unused")
    public void onEvent(DatabaseHelper.PlaylistsUpdatedEvent event) {
        if (mPlaylist != null && mPlaylist.getId().equals(event.mPlaylistId)) {
            mPlaylist = DatabaseHelper.get().getPlaylist(mPlaylist.getId());
            scheduleUpdateAdapter();
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(CollectionManager.UpdatedEvent event) {
        super.onEventMainThread(event);

        if (event.mUpdatedItemIds != null && event.mUpdatedItemIds.contains(mAlbum.getCacheKey())
                && mContainerFragmentClass == null) {
            showAlbumFancyDropDown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        User.getSelf().done(new DoneCallback<User>() {
            @Override
            public void onDone(User user) {
                if (mUser != null) {
                    if (mShowMode == SHOW_MODE_PLAYBACKLOG) {
                        String requestId = InfoSystem.get().resolvePlaybackLog(mUser);
                        if (requestId != null) {
                            mCorrespondingRequestIds.add(requestId);
                        }
                    } else if (mShowMode == SHOW_MODE_LOVEDITEMS) {
                        mHideRemoveButton = true;
                        if (mUser == user) {
                            CollectionManager.get().fetchLovedItemsPlaylist();
                        } else {
                            String requestId = InfoSystem.get().resolveLovedItems(mUser);
                            if (requestId != null) {
                                mCorrespondingRequestIds.add(requestId);
                            }
                        }
                    }
                    if (mUser != user) {
                        mHideRemoveButton = true;
                    } else {
                        CollectionManager.get().fetchPlaylists();
                    }
                } else {
                    mHideRemoveButton = true;
                }
                updateAdapter();
            }
        });
        if (mContainerFragmentClass == null) {
            getActivity().setTitle("");
        }
    }

    /**
     * Called every time an item inside a ListView or GridView is clicked
     *
     * @param view    the clicked view
     * @param item    the Object which corresponds to the click
     * @param segment
     */
    @Override
    public void onItemClick(View view, Object item, Segment segment) {
        if (getMediaController() == null) {
            Log.e(TAG, "onItemClick failed because getMediaController() is null");
            return;
        }
        if (item instanceof PlaylistEntry) {
            PlaylistEntry entry = (PlaylistEntry) item;
            if (entry.getQuery().isPlayable()) {
                if (getPlaybackManager().getCurrentEntry() == entry) {
                    // if the user clicked on an already playing track
                    int playState = getMediaController().getPlaybackState().getState();
                    if (playState == PlaybackStateCompat.STATE_PLAYING) {
                        getMediaController().getTransportControls().pause();
                    } else if (playState == PlaybackStateCompat.STATE_PAUSED) {
                        getMediaController().getTransportControls().play();
                    }
                } else {
                    getPlaybackManager().setPlaylist(mCurrentPlaylist, entry);
                    getMediaController().getTransportControls().play();
                }
            }
        }
    }

    /**
     * Update this {@link org.runbuddy.tomahawk_android.ui.fragments.TomahawkFragment}'s {@link
     * org.runbuddy.tomahawk_android.adapters.TomahawkListAdapter} content
     */
    @Override
    protected void updateAdapter() {
        if (!mIsResumed) {
            return;
        }

        if (mAlbum != null) {
            showContentHeader(mAlbum);
            if (mContainerFragmentClass == null) {
                showAlbumFancyDropDown();
            }
            mCollection.getAlbumTracks(mAlbum).done(new DoneCallback<Playlist>() {
                @Override
                public void onDone(Playlist playlist) {
                    mCurrentPlaylist = playlist;
                    Segment.Builder builder = new Segment.Builder(playlist)
                            .headerLayout(R.layout.single_line_list_header)
                            .headerString(mAlbum.getArtist().getPrettyName());
                    if (playlist != null && playlist.allFromOneArtist()) {
                        builder.hideArtistName(true);
                        builder.showDuration(true);
                    }
                    builder.showNumeration(true, 1);
                    fillAdapter(builder.build());
                    //Log.i(TAG_PLAYLIST, mCurrentPlaylist.toString());//set tag in 2016.08.01
                }
            });
        } else if (mUser != null || mPlaylist != null) {
            if (mUser != null) {
                if (mShowMode == SHOW_MODE_PLAYBACKLOG) {
                    mCurrentPlaylist = mUser.getPlaybackLog();
                } else if (mShowMode == SHOW_MODE_LOVEDITEMS) {
                    mCurrentPlaylist = mUser.getFavorites();
                }
            }
            if (mPlaylist != null) {
                mCurrentPlaylist = mPlaylist;
            }
            if (!mCurrentPlaylist.isFilled()) {
                User.getSelf().done(new DoneCallback<User>() {
                    @Override
                    public void onDone(User user) {
                        if (mShowMode < 0) {
                            if (mUser != user) {
                                String requestId = InfoSystem.get().resolve(mCurrentPlaylist);
                                if (requestId != null) {
                                    mCorrespondingRequestIds.add(requestId);
                                }
                            } else if (mPlaylist != null) {
                                mPlaylist = DatabaseHelper.get().getPlaylist(mPlaylist.getId());
                                updateAdapter();
                            }
                        }
                    }
                });
            } else {
                Segment.Builder builder = new Segment.Builder(mCurrentPlaylist);
                if (mContainerFragmentClass == null
                        || !mContainerFragmentClass.equals(SearchPagerFragment.class.getName())) {
                    builder.showNumeration(true, 1);
                    if (mContainerFragmentClass == null || !mContainerFragmentClass
                            .equals(ChartsPagerFragment.class.getName())) {
                        builder.headerLayout(R.layout.single_line_list_header)
                                .headerString(R.string.playlist_details);
                    }
                } else if (mContainerFragmentClass.equals(SearchPagerFragment.class.getName())) {
                    builder.showResolverIcon(true);
                }
                Segment segment = builder.build();
                fillAdapter(segment);
                showContentHeader(mCurrentPlaylist);
                showFancyDropDown(0, mCurrentPlaylist.getName(), null, null);
                ThreadManager.get()
                        .execute(new TomahawkRunnable(TomahawkRunnable.PRIORITY_IS_INFOSYSTEM_LOW) {
                            @Override
                            public void run() {
                                if (mCurrentPlaylist.getTopArtistNames() == null
                                        || mCurrentPlaylist.getTopArtistNames().length == 0) {
                                    boolean isFavorites = mUser != null
                                            && mCurrentPlaylist == mUser.getFavorites();
                                    mCurrentPlaylist.updateTopArtistNames(isFavorites);
                                } else {
                                    for (int i = 0; i < mCurrentPlaylist.getTopArtistNames().length
                                            && i < 5; i++) {
                                        String artistName = mCurrentPlaylist.getTopArtistNames()[i];
                                        if (!mResolvingTopArtistNames.contains(artistName)) {
                                            String requestId = InfoSystem.get()
                                                    .resolve(Artist.get(artistName), false);
                                            if (requestId != null) {
                                                mCorrespondingRequestIds.add(requestId);
                                            }
                                            mResolvingTopArtistNames.add(artistName);
                                        }
                                    }
                                }
                            }
                        });
            }
        } else {
            mCollection.getQueries(getSortMode()).done(new DoneCallback<Playlist>() {
                @Override
                public void onDone(final Playlist playlist) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentPlaylist = playlist;
                            String id = mCollection.getId();
                            Segment segment = new Segment.Builder(playlist)
                                    .headerLayout(R.layout.dropdown_header)
                                    .headerStrings(constructDropdownItems())
                                    .spinner(constructDropdownListener(
                                            COLLECTION_TRACKS_SPINNER_POSITION + id),
                                            getDropdownPos(COLLECTION_TRACKS_SPINNER_POSITION + id))
                                    .build();
                            fillAdapter(segment);
                        }
                    }).start();
                }
            });
        }
    }

    private List<Integer> constructDropdownItems() {
        List<Integer> dropDownItems = new ArrayList<>();
        if (!(mCollection instanceof ScriptResolverCollection)) {
            dropDownItems.add(R.string.collection_dropdown_recently_added);
        }
        dropDownItems.add(R.string.collection_dropdown_alpha);
        dropDownItems.add(R.string.collection_dropdown_alpha_artists);
        return dropDownItems;
    }

    private int getSortMode() {
        String id = mCollection.getId();
        int pos = getDropdownPos(COLLECTION_TRACKS_SPINNER_POSITION + id);
        if (!(mCollection instanceof ScriptResolverCollection)) {
            switch (pos) {
                case 0:
                    return Collection.SORT_LAST_MODIFIED;
                case 1:
                    return Collection.SORT_ALPHA;
                case 2:
                    return Collection.SORT_ARTIST_ALPHA;
                default:
                    return Collection.SORT_NOT;
            }
        } else {
            switch (pos) {
                case 0:
                    return Collection.SORT_ALPHA;
                case 1:
                    return Collection.SORT_ARTIST_ALPHA;
                default:
                    return Collection.SORT_NOT;
            }
        }
    }

    private void showAlbumFancyDropDown() {
        if (mAlbum != null) {
            CollectionManager.get().getAvailableCollections(mAlbum).done(
                    new DoneCallback<List<Collection>>() {
                        @Override
                        public void onDone(final List<Collection> result) {
                            int initialSelection = 0;
                            for (int i = 0; i < result.size(); i++) {
                                if (result.get(i) == mCollection) {
                                    initialSelection = i;
                                    break;
                                }
                            }
                            showFancyDropDown(initialSelection, mAlbum.getPrettyName(),
                                    FancyDropDown.convertToDropDownItemInfo(result),
                                    new FancyDropDown.DropDownListener() {
                                        @Override
                                        public void onDropDownItemSelected(int position) {
                                            mCollection = result.get(position);
                                            updateAdapter();
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });
                        }
                    });
        }
    }
}
