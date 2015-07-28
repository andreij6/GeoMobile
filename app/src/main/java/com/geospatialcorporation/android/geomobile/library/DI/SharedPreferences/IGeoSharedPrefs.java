package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;

import java.util.List;

public interface IGeoSharedPrefs {
    void add(List<String> item_table, String entity);

    String get(List<String> googleAccountName);

    void remove(List<String> googleAccountName);
}
