package com.geospatialcorporation.android.geomobile.library.util;

public class LoginValidator {

    public static boolean isEmailValid(String email){
        return (!email.isEmpty()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        // TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
