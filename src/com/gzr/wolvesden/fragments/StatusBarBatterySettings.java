/*
 * Copyright (C) GZOSP
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

package com.gzr.wolvesden.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.gzr.wolvesden.preference.CustomSeekBarPreference;
import com.gzr.wolvesden.preference.SystemSettingSwitchPreference;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import java.util.ArrayList;
import java.util.List;

public class StatusBarBatterySettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String BATTERY_STYLE = "battery_style";
    private static final String BATTERY_PERCENT = "battery_percent";

    private ListPreference mBatteryIconStyle;
    private ListPreference mBatteryPercentStyle;

    private static final String KEY_ESTIMATE_IN_QQS = "show_battery_estimate_qqs";

    private SystemSettingSwitchPreference mShowBatteryInQQS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar_battery_settings);
        final ContentResolver resolver = getActivity().getContentResolver();

        mBatteryIconStyle = (ListPreference) findPreference(BATTERY_STYLE);
        mBatteryIconStyle.setValue(Integer.toString(Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 0)));
        mBatteryIconStyle.setOnPreferenceChangeListener(this);
        mBatteryPercentStyle = (ListPreference) findPreference(BATTERY_PERCENT);
        mBatteryPercentStyle.setValue(Integer.toString(Settings.Secure.getInt(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 0)));
        mBatteryPercentStyle.setOnPreferenceChangeListener(this);

        // Battery estimate in Quick QS
        mShowBatteryInQQS = (SystemSettingSwitchPreference) findPreference(KEY_ESTIMATE_IN_QQS);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.VALIDUS;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
       if (preference == mBatteryIconStyle) {
            int value = Integer.valueOf((String) newValue);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, value);
            return true;
        }
        else if (preference == mBatteryPercentStyle) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SHOW_BATTERY_PERCENT, value);
            return true;
	 } 
        return false;
    }

    private void updateShowBatteryInQQS(int value) {
        switch (value) {
            case 1: {
                mShowBatteryInQQS.setEnabled(true);
                break;
            }
            case 0:
            case 2: {
                mShowBatteryInQQS.setEnabled(false);
                break;
            }
        }
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.statusbar_battery_settings;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
