package com.geospatialcorporation.android.geomobile.models.Query.quickSearch;

import java.util.List;

public class QuickSearchResponse {
    //region Getters & Setters
    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public List<QuickSearchResult> getResults() {
        return Results;
    }

    public void setResults(List<QuickSearchResult> results) {
        Results = results;
    }
    //endregion

    int Type;
    List<QuickSearchResult> Results;
}
