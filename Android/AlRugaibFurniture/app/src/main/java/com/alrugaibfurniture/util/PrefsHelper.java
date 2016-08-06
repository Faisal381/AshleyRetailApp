package com.alrugaibfurniture.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alrugaibfurniture.FurnitureApplication;

/**
 * Helper class to provide simpler use of SharedPreferences
 */

public class PrefsHelper {
    public static final String SHARED_PREFERENCES = "FurniturePrefs";
    private static final SharedPreferences sSharedPreferences = FurnitureApplication
            .getInstance().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

    private static final String SHARED_PREVIOUS_LANGUAGE = SHARED_PREFERENCES + ".PreviousLanguage";

    public static void clearPrefs() {
        sSharedPreferences.edit().clear().commit();
    }

    public static boolean isEnglishLanguage() {
        return sSharedPreferences.getBoolean(SHARED_PREVIOUS_LANGUAGE, true);
    }

    public static void setLanguage(boolean isEnglish) {
        sSharedPreferences.edit().putBoolean(SHARED_PREVIOUS_LANGUAGE, isEnglish).apply();
    }


}
