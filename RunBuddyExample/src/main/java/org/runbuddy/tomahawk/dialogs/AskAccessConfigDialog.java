/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2015, Enno Gottschalk <mrmaffen@googlemail.com>
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
package org.runbuddy.tomahawk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.runbuddy.R;
import org.runbuddy.libtomahawk.authentication.HatchetAuthenticatorUtils;
import org.runbuddy.libtomahawk.resolver.HatchetStubResolver;
import org.runbuddy.tomahawk.app.TomahawkApp;


/**
 * A {@link android.support.v4.app.DialogFragment} which is being shown to the user to ask him to
 * give us the notification listener permission, so that we can also scrobble to hatchet when music
 * is being played in other apps.
 */
public class AskAccessConfigDialog extends ConfigDialog {

    public final static String TAG = AskAccessConfigDialog.class.getSimpleName();

    /**
     * Called when this {@link android.support.v4.app.DialogFragment} is being created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        addScrollingViewToFrame(R.layout.config_ask_access);
        setDialogTitle(HatchetAuthenticatorUtils.HATCHET_PRETTY_NAME);
        onResolverStateUpdated(HatchetStubResolver.get());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getDialogView());
        return builder.create();
    }

    @Override
    protected void onPositiveAction() {
        try {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TomahawkApp.getContext(),
                            R.string.notification_settings_activity_not_found, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        dismiss();
    }
}
