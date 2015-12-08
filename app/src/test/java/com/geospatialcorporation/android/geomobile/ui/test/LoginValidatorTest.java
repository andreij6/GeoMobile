package com.geospatialcorporation.android.geomobile.ui.test;

import com.geospatialcorporation.android.geomobile.library.util.LoginValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginValidatorTest {

    //region EmailIsValid
    @Test
    public void isEmailValid_True(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones@geospatialcorp.com");

        assertEquals(true, actual);
    }

    @Test
    public void isEmailValid_False(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones?geospatialcorp.com");

        assertEquals(false, actual);
    }

    @Test
    public void isEmailValid_False_1(){
        Boolean actual = LoginValidator.isEmailValid("@andre.jonesgeospatialcorp.com");

        assertEquals(false, actual);
    }

    @Test
    public void isEmailValid_False_2(){
        Boolean actual = LoginValidator.isEmailValid("andre.jones@geospatialcorp");

        assertEquals(false, actual);
    }

    @Test
    public void isEmailValid_False_3(){
        Boolean actual = LoginValidator.isEmailValid("@.com");

        assertEquals(false, actual);
    }
    //endregion

    //region Password Valid

    //endregion
}
