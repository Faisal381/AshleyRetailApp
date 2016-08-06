package com.alrugaibfurniture.util;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {


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


    public static boolean validCellPhone(String number)
    {
        return Patterns.PHONE.matcher(number).matches();
    }

}
