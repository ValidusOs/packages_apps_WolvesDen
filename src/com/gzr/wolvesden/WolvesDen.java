/*
 * Copyright (C) 2017-2018 The Dirty Unicorns Project
 * Copyright (C) 2019 GroundZero
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gzr.wolvesden;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.gzr.wolvesden.navigation.BottomNavigationViewCustom;
import com.gzr.wolvesden.tabs.System;
import com.gzr.wolvesden.tabs.Style;
import com.gzr.wolvesden.tabs.Lockscreen;
import com.gzr.wolvesden.tabs.StatusBar;
import com.gzr.wolvesden.tabs.Navigation;


public class WolvesDen extends SettingsPreferenceFragment {

    private MenuItem menuitem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wolvesden, container, false);

        final BottomNavigationViewCustom navigation = view.findViewById(R.id.navigation);
        navigation.setBackground(new ColorDrawable(
                getResources().getColor(R.color.BottomBarBackgroundColor)));

        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationViewCustom.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.system:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.style:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.lockscreen:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.statusbar:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(menuitem != null) {
                    menuitem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                menuitem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new System();
            frags[1] = new Style();
            frags[2] = new Lockscreen();
            frags[3] = new StatusBar();
            frags[4] = new Navigation();

        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
                    getString(R.string.system_category),
                    getString(R.string.style_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.navigation_category)};
        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.VALIDUS;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, R.string.wolvesden_summary_title);
    }

    private static void showDialog(Fragment context, DialogFragment dialog) {
        FragmentTransaction ft = context.getChildFragmentManager().beginTransaction();
        Fragment prev = context.getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialog.show(ft, "dialog");
    }
}
