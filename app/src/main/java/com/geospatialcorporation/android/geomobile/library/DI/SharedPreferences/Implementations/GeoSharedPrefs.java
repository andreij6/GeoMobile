package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations;

import android.content.Context;
import android.content.SharedPreferences;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.IGeoSharedPrefs;
import java.util.Arrays;
import java.util.List;


public class GeoSharedPrefs implements IGeoSharedPrefs {

    private Context mContext;
    private static final int Table_Index = 1;
    private static final int Key_Index = 0;

    public GeoSharedPrefs(){
        mContext = application.getAppContext();
    }

    @Override
    public void add(List<String> item_table, String entity) {

        SharedPreferences preferences = mContext.getSharedPreferences(item_table.get(Table_Index), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(item_table.get(Key_Index), entity);
        editor.apply();

    }

    @Override
    public String get(List<String> item_table) {
        SharedPreferences preferences = mContext.getSharedPreferences(item_table.get(Table_Index), Context.MODE_PRIVATE);

        return preferences.getString(item_table.get(Key_Index), null);
    }

    @Override
    public void remove(List<String> item_table) {
        SharedPreferences preferences = mContext.getSharedPreferences(item_table.get(Table_Index), Context.MODE_PRIVATE);
        preferences.edit().remove(item_table.get(Key_Index)).commit();
    }

    public static class Items {
        public static List<String> GOOGLE_ACCOUNT_NAME = Arrays.asList("Google Account Name", "GeoAuthentication");
    }
}
