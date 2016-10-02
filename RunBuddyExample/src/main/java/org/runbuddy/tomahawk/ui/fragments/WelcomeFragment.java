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
package org.runbuddy.tomahawk.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.runbuddy.R;
import org.runbuddy.libtomahawk.authentication.AuthenticatorManager;
import org.runbuddy.libtomahawk.authentication.HatchetAuthenticatorUtils;
import org.runbuddy.tomahawk.TomahawkApp;
import org.runbuddy.tomahawk.activities.TomahawkMainActivity;
import org.runbuddy.tomahawk.utils.PreferenceUtils;
import org.runbuddy.tomahawk.views.HatchetLoginRegisterView;
import org.runbuddy.tomahawk.views.SimplePagerIndicator;

import de.greenrobot.event.EventBus;

/**
 * A {@link android.support.v4.app.Fragment} which is being shown to the user when he first opens
 * the app
 */
public class WelcomeFragment extends Fragment {

    public final static String TAG = WelcomeFragment.class.getSimpleName();

    private ViewPager mViewPager;

    private TextView mPositiveButton;

    private HatchetLoginRegisterView mHatchetLoginRegisterView;

    private class LoginRegisterPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TomahawkMainActivity activity = (TomahawkMainActivity) getActivity();
            LayoutInflater inflater = activity.getLayoutInflater();
            View v = null;
            switch (position) {
                case 0:
                    v = inflater.inflate(R.layout.welcome_fragment_page_explanation, container,
                            false);
                    break;
                case 1:
                    v = inflater.inflate(R.layout.welcome_fragment_page_setup, container,
                            false);

                    Bundle args = new Bundle();
                    args.putString(TomahawkFragment.CONTAINER_FRAGMENT_CLASSNAME,
                            WelcomeFragment.class.getName());
                    args.putInt(TomahawkFragment.CONTAINER_FRAGMENT_PAGE, position);
                    args.putInt(TomahawkFragment.CONTENT_HEADER_MODE,
                            TomahawkFragment.MODE_HEADER_NONE);
                    Fragment fragment = Fragment.instantiate(
                            activity, PreferenceConnectFragment.class.getName(), args);
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.add(R.id.welcome_fragment_page_setup_container, fragment);
                    ft.commitAllowingStateLoss();
                    break;
                case 2:
                    v = inflater.inflate(R.layout.welcome_fragment_page_hatchet, container,
                            false);
                    ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.smoothprogressbar);
                    HatchetAuthenticatorUtils authenticatorUtils = (HatchetAuthenticatorUtils)
                            AuthenticatorManager.get().getAuthenticatorUtils(
                                    TomahawkApp.PLUGINNAME_HATCHET);
                    mHatchetLoginRegisterView =
                            (HatchetLoginRegisterView) v.findViewById(R.id.hatchetloginregister);
                    mHatchetLoginRegisterView.setup(authenticatorUtils, progressBar);
                    break;
                case 3:
                    v = inflater.inflate(R.layout.welcome_fragment_page_done, container,
                            false);
                    break;
            }
            if (v != null) {
                container.addView(v);
            }
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int lastPage = mViewPager.getAdapter().getCount() - 1;
            if (position == lastPage) {
                mPositiveButton.setText(R.string.ok);
            } else {
                mPositiveButton.setText(R.string.next_page);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @SuppressWarnings("unused")
    public void onEventMainThread(AuthenticatorManager.ConfigTestResultEvent event) {
        if (mHatchetLoginRegisterView != null) {
            mHatchetLoginRegisterView
                    .onConfigTestResult(event.mComponent, event.mType, event.mMessage);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPositiveButton = (TextView) view.findViewById(R.id.config_dialog_positive_button);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastPage = mViewPager.getAdapter().getCount() - 1;
                if (mViewPager.getCurrentItem() == lastPage) {
                    PreferenceUtils.edit().putBoolean(
                            PreferenceUtils.COACHMARK_WELCOMEFRAGMENT_DISABLED, true).apply();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        });
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new LoginRegisterPagerAdapter());
        SimplePagerIndicator indicator =
                (SimplePagerIndicator) view.findViewById(R.id.simplepagerindicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(mOnPageChangeListener);
    }

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
