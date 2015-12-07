package com.geospatialcorporation.android.geomobile.models.Query.quickSearch;

public class QuickSearchRequest {
    //region Getters & Setters
    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
    //endregion

    String Text;

    public QuickSearchRequest(String search){
        Text = search;
    }

}
