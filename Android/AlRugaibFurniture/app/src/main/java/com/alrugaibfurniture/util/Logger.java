package com.alrugaibfurniture.util;

import android.util.Log;

import com.alrugaibfurniture.BuildConfig;

/**
 * This class extends functionality of Logs with turning off when BuildConfig is Release.
 * In this class should be added future Crashlytics logs if We ever add it.
 */
public class Logger {

    /**
     * Log wrapper for Debug option
     *
     * @param tag     tag for log
     * @param message message of log
     */
    public static void logD(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
}
