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
package org.runbuddy.tomahawk_android.ui.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.runbuddy.libtomahawk.authentication.AuthenticatorManager;
import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.ListItemString;
import org.runbuddy.libtomahawk.resolver.HatchetStubResolver;
import org.runbuddy.libtomahawk.resolver.PipeLine;
import org.runbuddy.libtomahawk.resolver.Resolver;
import org.runbuddy.libtomahawk.resolver.ScriptResolver;
import org.runbuddy.libtomahawk.resolver.UserCollectionStubResolver;
import org.runbuddy.tomahawk_android.TomahawkApp;
import org.runbuddy.tomahawk_android.activities.TomahawkMainActivity;
import org.runbuddy.tomahawk_android.adapters.Segment;
import org.runbuddy.tomahawk_android.adapters.TomahawkListAdapter;
import org.runbuddy.tomahawk_android.dialogs.ConfigDialog;
import org.runbuddy.tomahawk_android.dialogs.DirectoryChooserConfigDialog;
import org.runbuddy.tomahawk_android.dialogs.GMusicConfigDialog;
import org.runbuddy.tomahawk_android.dialogs.HatchetLoginDialog;
import org.runbuddy.tomahawk_android.dialogs.ResolverConfigDialog;
import org.runbuddy.tomahawk_android.dialogs.ResolverRedirectConfigDialog;
import org.runbuddy.tomahawk_android.listeners.MultiColumnClickListener;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link org.runbuddy.tomahawk_android.ui.fragments.TomahawkListFragment} which fakes the standard
 * {@link android.preference.PreferenceFragment} behaviour. We need to fake it, because the official
 * support library doesn't provide a {@link android.preference.PreferenceFragment} class
 */
public class PreferenceConnectFragment extends TomahawkListFragment
        implements MultiColumnClickListener {

    private static final String TAG = PreferenceConnectFragment.class.getSimpleName();

    @SuppressWarnings("unused")
    public void onEventMainThread(CollectionManager.UpdatedEvent event) {
        getListAdapter().notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AuthenticatorManager.ConfigTestResultEvent event) {
        getListAdapter().notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ScriptResolver.EnabledStateChangedEvent event) {
        getListAdapter().notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(PipeLine.ResolversChangedEvent event) {
        updateAdapter();
    }

    /**
     * Called, when this {@link org.runbuddy.tomahawk_android.ui.fragments.PreferenceConnectFragment}'s
     * {@link View} has been created
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateAdapter();
    }

    private void updateAdapter() {
        List<Segment> segments = new ArrayList<>();

        // Add the header text item
        List<ListItemString> textItems = new ArrayList<>();
        textItems.add(new ListItemString(getString(R.string.connect_headertext)));
        Segment segment = new Segment.Builder(textItems).build();
        segments.add(segment);

        // Add all resolver grid items
        List<Resolver> resolvers = new ArrayList<>();
        resolvers.add(UserCollectionStubResolver.get());
        if (mContainerFragmentClass == null
                || !mContainerFragmentClass.equals(WelcomeFragment.class.getName())) {
            resolvers.add(HatchetStubResolver.get());
        }
        List<ScriptResolver> scriptResolvers = PipeLine.get().getScriptResolvers();
        Collections.sort(scriptResolvers, new Comparator<ScriptResolver>() {
            @Override
            public int compare(ScriptResolver lhs, ScriptResolver rhs) {
                return lhs.getPrettyName().compareToIgnoreCase(rhs.getPrettyName());
            }
        });
        for (ScriptResolver scriptResolver : scriptResolvers) {
            //TODO: Remove this hack once we can get rid of Tomahawk.resolver.instance completely (see ScriptAccount#onWebViewClientReady)
            if (!scriptResolver.getId().contains("-metadata")
                    && !scriptResolver.getId().equals("echonest")
                    && !scriptResolver.getId().equals("itunes")
                    && !scriptResolver.getScriptAccount().isManuallyInstalled()) {
                resolvers.add(scriptResolver);
            }
        }
        segment = new Segment.Builder(resolvers)
                .showAsGrid(R.integer.grid_column_count, R.dimen.padding_superlarge,
                        R.dimen.padding_superlarge)
                .build();
        segments.add(segment);

        resolvers = new ArrayList<>();
        for (ScriptResolver scriptResolver : scriptResolvers) {
            if (!scriptResolver.getId().contains("-metadata")
                    && scriptResolver.getScriptAccount().isManuallyInstalled()) {
                resolvers.add(scriptResolver);
            }
        }
        segment = new Segment.Builder(resolvers)
                .headerLayout(R.layout.single_line_list_header)
                .headerString(R.string.connect_header_manualresolvers)
                .showAsGrid(R.integer.grid_column_count, R.dimen.padding_superlarge,
                        R.dimen.padding_superlarge)
                .build();
        segments.add(segment);

        if (getListView() != null) {
            if (getListAdapter() == null) {
                TomahawkListAdapter tomahawkListAdapter = new TomahawkListAdapter(
                        (TomahawkMainActivity) getActivity(), getActivity().getLayoutInflater(),
                        segments, getListView(), this);
                setListAdapter(tomahawkListAdapter);
            } else {
                ((TomahawkListAdapter) getListAdapter()).setSegments(segments, getListView());
            }

            setupNonScrollableSpacer(getListView());
        } else {
            Log.d(TAG, "Couldn't update adapter because getListView() returned null!");
        }
    }

    @Override
    public void onItemClick(View view, Object item, Segment segment) {
        if (item instanceof Resolver) {
            String id = ((Resolver) item).getId();
            ConfigDialog dialog;
            switch (id) {
                case TomahawkApp.PLUGINNAME_SPOTIFY:
                case TomahawkApp.PLUGINNAME_DEEZER:
                    dialog = new ResolverRedirectConfigDialog();
                    break;
                case TomahawkApp.PLUGINNAME_USERCOLLECTION:
                    dialog = new DirectoryChooserConfigDialog();
                    break;
                case TomahawkApp.PLUGINNAME_HATCHET:
                    dialog = new HatchetLoginDialog();
                    break;
                case TomahawkApp.PLUGINNAME_GMUSIC:
                    AccountManager accountManager = AccountManager.get(TomahawkApp.getContext());
                    Account[] accounts = accountManager.getAccountsByType("com.google");
                    if (accounts != null && accounts.length > 0) {
                        dialog = new GMusicConfigDialog();
                    } else {
                        dialog = new ResolverConfigDialog();
                    }
                    break;
                default:
                    dialog = new ResolverConfigDialog();
                    break;
            }
            Bundle args = new Bundle();
            args.putString(TomahawkFragment.PREFERENCEID, id);
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public boolean onItemLongClick(View view, Object item, Segment segment) {
        return false;
    }
}
