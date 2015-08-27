package com.geospatialcorporation.android.geomobile.models.Login;

public class ErrorResponse {
    private String Code;
    private String HttpCode;
    private String Message;

    public String getCode() { return Code; }
    public String getHttpCode() { return HttpCode; }
    public String getMessage() { return Message; }

    ErrorResponse() {

    }
}
