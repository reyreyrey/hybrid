package com.ivi.hybrid.core.modules.cache;

import android.app.Application;
import android.content.SharedPreferences;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.LogUtil;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener;
import net.grandcentrix.tray.core.TrayItem;

import java.util.Collection;

/**
 * Created by Rea.X on 2017/2/2.
 */

public class PreferencesHelper {
    private static AppPreferences preferences;

    static {
        preferences = new AppPreferences(Hybrid.get());
        preferences.registerOnTrayPreferenceChangeListener(new Listener());
    }

    private static class Listener implements OnTrayPreferenceChangeListener {

        @Override
        public void onTrayPreferenceChanged(Collection<TrayItem> items) {
            for (TrayItem i : items) {
                LogUtil.d("tray:::::" + i.toString());
            }
        }
    }

    private static AppPreferences getPreferences() {
        if (preferences == null)
            preferences = new AppPreferences(Hybrid.get());
        return preferences;
    }

    static String getString(String key) {
        return getPreferences().getString(key, null);
    }

    static void saveString(String key, String value) {
        getPreferences().put(key, value);
    }

    static int getInt(String key) {
        return getPreferences().getInt(key, 0);
    }

    static void saveInt(String key, int value) {
        getPreferences().put(key, value);
    }

    static boolean getBoolean(String key) {
        return getPreferences().getBoolean(key, false);
    }

    static void saveBoolean(String key, boolean value) {
        getPreferences().put(key, value);
    }

    static void delete(String key) {
        getPreferences().remove(key);
    }
}
