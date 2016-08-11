package com.alrugaibfurniture;

import android.app.Application;

/**
 * Singleton Application for easy access to context
 */
public class FurnitureApplication extends Application {

    private static FurnitureApplication instance;

    public static FurnitureApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
