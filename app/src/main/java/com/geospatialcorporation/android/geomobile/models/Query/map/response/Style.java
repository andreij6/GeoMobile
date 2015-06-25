package com.geospatialcorporation.android.geomobile.models.Query.map.response;

/**
 * Created by andre on 6/24/2015.
 */
public class Style {
    //region Getters & Setter
    public String getBorderColor() {
        return BorderColor;
    }

    public void setBorderColor(String borderColor) {
        BorderColor = borderColor;
    }

    public Integer getBorderWidth() {
        return BorderWidth;
    }

    public void setBorderWidth(Integer borderWidth) {
        BorderWidth = borderWidth;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getLineStyleCode() {
        return LineStyleCode;
    }

    public void setLineStyleCode(Integer lineStyleCode) {
        LineStyleCode = lineStyleCode;
    }

    public Integer getPointStyleCode() {
        return PointStyleCode;
    }

    public void setPointStyleCode(Integer pointStyleCode) {
        PointStyleCode = pointStyleCode;
    }

    public Integer getWidth() {
        return Width;
    }

    public void setWidth(Integer width) {
        Width = width;
    }
    //endregion

    String BorderColor;
    Integer BorderWidth;
    Integer Id;
    Integer LineStyleCode;
    Integer PointStyleCode;
    Integer Width;


}