package com.alrugaibfurniture.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alrugaibfurniture.R;
import com.alrugaibfurniture.communication.ApiHelper;
import com.alrugaibfurniture.model.CustomerProfile;
import com.alrugaibfurniture.util.Logger;
import com.alrugaibfurniture.util.PrefsHelper;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Activity that contains change of language and input of mobile number
 */
public class LoginActivity extends Activity {

    private static final String EXTRA_NUMBER = "Number";

    @Bind(R.id.flag_arabic)
    TextView flagArabic;
    @Bind(R.id.flag_english)
    TextView flagEnglish;
    @Bind(R.id.input_phone)
    EditText phoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_NUMBER)) {
            phoneInput.setText(getIntent().getStringExtra(EXTRA_NUMBER));
        }
        initView();

    }

    private void initView() {
        //Set border for language chosen
        flagArabic.setBackground(PrefsHelper.isEnglishLanguage() ? null : getDrawable(R.drawable.flag_background_border));
        flagEnglish.setBackground(PrefsHelper.isEnglishLanguage() ? getDrawable(R.drawable.flag_background_border) : null);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        phoneInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        // transformation = null to avoid ** in number
        phoneInput.setTransformationMethod(null);
        imm.showSoftInput(phoneInput, InputMethodManager.SHOW_IMPLICIT);
        phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //On enter clicked in soft input
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (phoneInput.getText().length() >= 7 && phoneInput.getText().length() <= 12) {
                        ApiHelper.getInstance().login(phoneInput.getText().toString(), new Callback<CustomerProfile>() {
                            @Override
                            public void onResponse(Response<CustomerProfile> response, Retrofit retrofit) {
                                Logger.logD("login", "onResponse");
                                if(response.code() == 200 || response.code() == 201) {
                                    Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                                    intent.putExtra(CustomerActivity.EXTRA_FROM_LOGIN, response.body());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Logger.logD("onFailure", "onResponse");
                            }
                        });
                    } else if (phoneInput.getText().length() == 0) {
                        Toast.makeText(LoginActivity.this, R.string.input_phone, Toast.LENGTH_SHORT).show();
                    } else if (phoneInput.getText().length() > 12) {
                        Toast.makeText(LoginActivity.this, R.string.phone_to_long, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.phone_to_short, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Method called when R.id.flag_arabic is clicked
     */
    @OnClick(R.id.flag_arabic)
    void onArabicFlagClicked() {
        changeBorderAndSetLanguage(false);
    }

    /**
     * Method called when R.id.flag_english is clicked
     */
    @OnClick(R.id.flag_english)
    void onEnglishFlagClicked() {
        changeBorderAndSetLanguage(true);
    }

    /**
     * Set selected language, restart activity to show new strings
     *
     * @param isEnglish true is english, false is arabic
     */
    private void changeBorderAndSetLanguage(boolean isEnglish) {
        PrefsHelper.setLanguage(isEnglish);
        updateResources(isEnglish ? "en" : "ar");
        Intent intent = getIntent();
        intent.putExtra(EXTRA_NUMBER, phoneInput.getText().toString());
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * Update view with new language
     *
     * @param language - language to set
     */
    private void updateResources(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
