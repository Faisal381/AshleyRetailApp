package com.alrugaibfurniture.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alrugaibfurniture.R;
import com.alrugaibfurniture.util.PrefsHelper;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that contains change of language and input of mobile number
 * Created by paulc_000 on 02.08.2016.
 */
public class LoginActivity extends Activity {

    @Bind(R.id.flag_arabic)
    TextView flagArabic;
    @Bind(R.id.flag_english)
    TextView flagEnglish;
    @Bind(R.id.login_coordinator)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.input_phone)
    EditText phoneInput;

   @Bind(R.id.input_layout_phone)
   TextInputLayout inputHint;

    private static final String EXTRA_NUMBER = "Number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(getIntent()!=null && getIntent().hasExtra(EXTRA_NUMBER)){
            phoneInput.setText(getIntent().getStringExtra(EXTRA_NUMBER));
        }
        flagArabic.setBackground(PrefsHelper.isEnglishLanguage() ? null : getDrawable(R.drawable.flag_background_border));
        flagEnglish.setBackground(PrefsHelper.isEnglishLanguage()  ? getDrawable(R.drawable.flag_background_border) : null );
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
       // phoneInput.setRawInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL| InputType.TYPE_CLASS_NUMBER);
        phoneInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        phoneInput.setTransformationMethod(null);
        imm.showSoftInput(phoneInput, InputMethodManager.SHOW_IMPLICIT);
        phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(phoneInput.getText().length() >= 9){
                        Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                        intent.putExtra(CustomerActivity.EXTRA_NUMBER, phoneInput.getText().toString());
                        startActivity(intent);
                    }else if(phoneInput.getText().length() == 0){
                        Toast.makeText(LoginActivity.this, R.string.input_phone,Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(LoginActivity.this, R.string.phone_to_short,Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.flag_arabic)
    void onArabicFlagClicked(){
        changeBorderAndSetLanguage(false);
    }

    @OnClick(R.id.flag_english)
    void onEnglishFlagClicked(){

        changeBorderAndSetLanguage(true);
    }

   /* @OnClick(R.id.login_submit)
    void onSubmitClicked(){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Not implemented, yet!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }*/


    private void changeBorderAndSetLanguage(boolean isEnglish){

        PrefsHelper.setLanguage(isEnglish);

        updateResources(isEnglish ? "en" : "ar");
        Intent intent = getIntent();
        intent.putExtra(EXTRA_NUMBER, phoneInput.getText().toString());
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
      //  updateView();
    }

    private void updateView() {
         phoneInput.setHint(getString(R.string.hint_phone_number));
         inputHint.setHint(getString(R.string.hint_phone_number));
    }


   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ButterKnife.unbind(this);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        flagArabic = (ImageView) findViewById(R.id.flag_arabic);
        flagEnglish = (ImageView) findViewById(R.id.flag_english);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator);

        Logger.logE("LoginActivity", newConfig.locale.getCountry());
    }*/

    private void updateResources(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
