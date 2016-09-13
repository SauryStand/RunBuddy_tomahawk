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
package org.runbuddy.tomahawk_android.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.EditText;

import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.Playlist;
import org.runbuddy.libtomahawk.database.DatabaseHelper;
import org.runbuddy.libtomahawk.infosystem.User;
import org.runbuddy.libtomahawk.utils.ViewUtils;
import org.runbuddy.tomahawk_android.activities.TomahawkMainActivity;
import org.runbuddy.tomahawk_android.ui.fragments.ContentHeaderFragment;
import org.runbuddy.tomahawk_android.ui.fragments.PlaylistEntriesFragment;
import org.runbuddy.tomahawk_android.ui.fragments.TomahawkFragment;
import org.runbuddy.tomahawk_android.widgets.ConfigEdittext;
import org.runbuddy.tomahawk_android.utils.FragmentUtils;
import org.tomahawk.tomahawk_android.R;

/**
 * A {@link DialogFragment} which is presented for the user so that he can choose a name for the
 * {@link org.runbuddy.libtomahawk.collection.Playlist} he intends to create
 */
public class CreatePlaylistDialog extends ConfigDialog {

    private User mUser;

    private Playlist mPlaylist;

    private EditText mNameEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Check if there is a playlist key in the provided arguments
        if (getArguments() != null) {
            if (getArguments().containsKey(TomahawkFragment.PLAYLIST)) {
                String playlistId = getArguments().getString(TomahawkFragment.PLAYLIST);
                mPlaylist = DatabaseHelper.get().getPlaylist(playlistId);
                if (mPlaylist == null) {
                    mPlaylist = Playlist.getByKey(playlistId);
                    if (mPlaylist == null) {
                        dismiss();
                    }
                }
            }
            if (getArguments().containsKey(TomahawkFragment.USER)) {
                mUser = User.getUserById(getArguments().getString(TomahawkFragment.USER));
                if (mUser == null) {
                    dismiss();
                }
            }
        }

        //set the proper flags for our edittext
        mNameEditText = (ConfigEdittext) addScrollingViewToFrame(R.layout.config_edittext);
        mNameEditText.setHint(R.string.name_playlist);
        mNameEditText.setOnEditorActionListener(mOnKeyboardEnterListener);

        ViewUtils.showSoftKeyboard(mNameEditText);

        //Set the textview's text to the proper title
        setDialogTitle(getString(R.string.create_playlist));

        setStatusImage(R.drawable.ic_action_playlist);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getDialogView());
        return builder.create();
    }

    /**
     * Persist a {@link org.runbuddy.libtomahawk.collection.Playlist} as a {@link
     * org.runbuddy.libtomahawk.collection.Playlist} in our database
     */
    private void savePlaylist() {
        if (mPlaylist != null) {
            String playlistName = TextUtils.isEmpty(mNameEditText.getText().toString())
                    ? getString(R.string.playlist)
                    : mNameEditText.getText().toString();
            mPlaylist.setName(playlistName);
            CollectionManager.get().createPlaylist(mPlaylist);
            Bundle bundle = new Bundle();
            bundle.putString(TomahawkFragment.USER, mUser.getCacheKey());
            bundle.putString(TomahawkFragment.PLAYLIST, mPlaylist.getCacheKey());
            bundle.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                    ContentHeaderFragment.MODE_HEADER_DYNAMIC);
            FragmentUtils.replace((TomahawkMainActivity) getActivity(),
                    PlaylistEntriesFragment.class, bundle);
        }
    }

    @Override
    protected void onPositiveAction() {
        savePlaylist();
        dismiss();
    }
}
