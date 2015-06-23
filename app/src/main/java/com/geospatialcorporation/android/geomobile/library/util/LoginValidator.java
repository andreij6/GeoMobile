package com.geospatialcorporation.android.geomobile.library.util;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Created by andre on 6/23/2015.
 */
public class LoginValidator {

    public static boolean isEmailValid(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isPasswordValid(String password) {
        // TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
