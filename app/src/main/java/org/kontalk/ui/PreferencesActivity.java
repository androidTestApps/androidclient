/*
 * Kontalk Android client
 * Copyright (C) 2015 Kontalk Devteam <devteam@kontalk.org>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.kontalk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import org.kontalk.Kontalk;
import org.kontalk.R;
import org.kontalk.authenticator.Authenticator;


/**
 * Preferences activity.
 * TODO convert to fragments layout
 * @author Daniele Ricci
 */
public final class PreferencesActivity extends ToolbarActivity implements PreferencesFragment.Callback {
    private static final String TAG_NESTED = "nested";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preferences_screen);
        setupToolbar(true);

        if (savedInstanceState == null) {
            Fragment fragment;

            // no account - redirect to bootstrap preferences
            if (Authenticator.getDefaultAccount(this) == null) {
                fragment = new BootstrapPreferences();
            }
            else {
                fragment = new PreferencesFragment();
            }

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return getSupportFragmentManager().findFragmentById(R.id.container)
                .onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // this if statement is necessary to navigate through nested and main fragments
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onNestedPreferenceSelected(int key) {
        Fragment fragment;
        switch (key) {
            case R.xml.privacy_preferences:
                fragment = new PrivacyPreferences();
                break;
            default:
                throw new IllegalArgumentException("unknown preference screen: " + key);
        }

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, fragment, TAG_NESTED)
            .addToBackStack(TAG_NESTED).commit();
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, PreferencesActivity.class);
        context.startActivityIfNeeded(intent, -1);
    }

}
