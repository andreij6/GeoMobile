package com.geospatialcorporation.android.geomobile.library.validation.requests;

import com.geospatialcorporation.android.geomobile.R;

import java.util.Objects;

/**
 * Created by andre on 6/19/2015.
 */
public class LayerAttributeValidateRequest extends ValidateRequestBase implements IValidateRequest{

    String mName;
    String mDefaultValue;
    String mColumnType;

    public LayerAttributeValidateRequest(String name, String defaultValue, String columnType){
        mName = name;
        mDefaultValue = defaultValue;
        mColumnType = columnType;
        mIsValid = true;

    }

    @Override
    public boolean isValid() {
        if(mColumnType == null){
            mMessage = getString(R.string.column_type_null);
            mIsValid = false;
            return mIsValid;
        }

        switch (mColumnType){
            case "Text":
                TextColumnValid();
                break;
            case "True/False":
                BooleanColumnValid();
                break;
            case "Date":
                DateColumnValid();
                break;
            case "DateTime":
                DateTimeColumnValid();
                break;
            case "Integer":
                IntegerColumnValid();
                break;
            case "Decimal":
                DecimalColumnValid();
                break;
        }

        if(mIsValid){
            mMessage = "Valid";
        }

        return mIsValid;
    }

    private void TextColumnValid() {
        String invalidMessage = getString(R.string.name_invalid);


    }

    private void BooleanColumnValid() {
        String invalidMessage = getString(R.string.name_invalid) + ". " + getString(R.string.boolean_type_invalid);

        isNamePresent();
        isDefaultValue(Boolean.TYPE);

    }

    private void DateColumnValid() {
        String invalidMessage = "";

        isNamePresent();

    }

    private void DateTimeColumnValid() {
        String invalidMessage = "";

        isNamePresent();

    }

    private void IntegerColumnValid() {
        String invalidMessage = "";

        isNamePresent();
        isDefaultValue(Integer.TYPE);

    }

    private void DecimalColumnValid() {
        String invalidMessage = "";

        isNamePresent();
        isDefaultValue(Double.TYPE);  //TODO: tEST
    }

    @Override
    public String explainValidity() {
        isValid();

        return mMessage;
    }

    //region Checks
    private void isNamePresent() {
        if(mName == null || mName.equals("")){
            mIsValid = false;
        }
    }

    private <T extends Object> void isDefaultValue(T type){
        if(type instanceof Integer){
            try {
                int result = Integer.parseInt(mDefaultValue);
            } catch (NumberFormatException e){
                mIsValid = false;
            }
        }

        if(type instanceof Boolean){
            if(!Objects.equals(mDefaultValue.toLowerCase(), "true") && !Objects.equals(mDefaultValue.toLowerCase(), "false")){
                mIsValid = false;
            }
        }

        if(type instanceof Double){
            try {
                Double.parseDouble(mDefaultValue);
            } catch (NumberFormatException e){
                mIsValid = false;
            }
        }


    }
    //endregion

}
