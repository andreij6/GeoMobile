package com.geospatialcorporation.android.geomobile.ui.test;

import com.geospatialcorporation.android.geomobile.library.util.LoginValidator;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoginValidatorTest {

    //region EmailIsValid
    @Test
    public void isEmailValid_True(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones@geospatialcorp.com");

        Boolean expected = true;

        assertTrue("A valid Email returned invalid", actual == expected);
    }

    @Test
    public void isEmailValid_False(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones?geospatialcorp.com");

        Boolean expected = false;

        assertTrue("An invalid Email was returned valid", actual == expected);
    }

    @Test
    public void isEmailValid_False_1(){
        Boolean actual = LoginValidator.isEmailValid("@andre.jonesgeospatialcorp.com");

        Boolean expected = false;

        assertTrue("An invalid Email was returned valid", actual == expected);
    }

    @Test
    public void isEmailValid_False_2(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones@geospatialcorp");

        Boolean expected = false;

        assertTrue("An invalid Email was returned valid", actual == expected);
    }

    @Test
    public void isEmailValid_False_3(){
        Boolean actual = LoginValidator.isEmailValid("@.com");

        Boolean expected = false;

        assertTrue("An invalid Email was returned valid", actual == expected);
    }
    //endregion

    //region Password Valid

    //endregion
}
