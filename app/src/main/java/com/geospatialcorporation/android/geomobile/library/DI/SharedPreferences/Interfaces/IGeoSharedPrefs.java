package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Interfaces;

import android.content.SharedPreferences;

public interface IGeoSharedPrefs extends SharedPreferences {
    void add(String item_table, String entity);

    void add(String item_table, float entity);

    void remove(String key);

    void apply();

    void commit();

    void add(String loginRemember, boolean value);
}
