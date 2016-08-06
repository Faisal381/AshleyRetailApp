package com.alrugaibfurniture.util;

import android.util.Log;

import com.alrugaibfurniture.BuildConfig;

/**
 * This class extends functionality of Logs with turning off when BuildConfig is Release.
 * In this class should be added future Crashlytics logs if We ever add it.
 */
public class Logger {
    public static void logD(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void logV(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void logI(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void logW(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void logE(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
