package com.alrugaibfurniture.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;

import com.alrugaibfurniture.R;
import com.alrugaibfurniture.util.PrefsHelper;

import java.util.Locale;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setLanguage(PrefsHelper.isEnglishLanguage() ? "en" : "ar");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Start next screen on touch anywhere
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
        return true;
    }


    private void setLanguage(String language) {
        // set language on start from pref
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
