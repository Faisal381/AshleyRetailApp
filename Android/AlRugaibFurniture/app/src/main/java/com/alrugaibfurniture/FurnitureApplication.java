package com.alrugaibfurniture;

import android.app.Application;

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
