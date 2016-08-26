package com.alrugaibfurniture.util;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with utils methods
 */
public class Util {

    /**
     * Method validating email with regex
     *
     * @param email - email to validate
     * @return true if email is valid , false if not
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Method validating phone number
     *
     * @param number - phone number to validate
     * @return true if email is valid , false if not
     */
    public static boolean validCellPhone(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    /**
     * Method checking if string is a digit
     * @param str - string to check
     * @return true if input is numberic, false else
     */
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /**
     * Method to hide soft input keyboard
     * @param ctx - context to get focused view
     */
    public static void hideKeyboard(Activity ctx){
        View view = ctx.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
