package com.geospatialcorporation.android.geomobile.models.Query.quickSearch;

/**
 * Created by andre on 6/14/2015.
 */
public class QuickSearchResultVM {

    //region Getters & Setters
    public Integer getIcon() {
        return Icon;
    }

    public void setIcon(Integer icon) {
        Icon = icon;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getFoundIn() {
        return "Results Found in " + FoundIn;
    }

    public void setFoundIn(String foundIn){
        FoundIn = foundIn;
    }
    //endregion

    Integer Icon;
    String Result;
    String FoundIn;

}
