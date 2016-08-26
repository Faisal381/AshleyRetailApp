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

    /**
     * Method for debug purpose to clear preferences of app
     */
    public static void clearPrefs() {
        sSharedPreferences.edit().clear().commit();
    }

    /**
     * Get from SharedPreferences current language
     *
     * @return true if current language is english, false if current language is Arabic
     */
    public static boolean isEnglishLanguage() {
        return sSharedPreferences.getBoolean(SHARED_PREVIOUS_LANGUAGE, true);
    }

    /**
     * Set in SharedPreferences current language (as flag)
     *
     * @param isEnglish true if current language is english, false if current language is Arabic
     */
    public static void setLanguage(boolean isEnglish) {
        sSharedPreferences.edit().putBoolean(SHARED_PREVIOUS_LANGUAGE, isEnglish).apply();
    }


}
