package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Interfaces.IGeoSharedPrefs;

import java.util.Map;
import java.util.Set;

public class GeoSharedPrefsBase implements SharedPreferences, IGeoSharedPrefs {

    protected SharedPreferences mPreferences;
    protected SharedPreferences.Editor mEditor;

    public GeoSharedPrefsBase(SharedPreferences preferences){
        mPreferences = preferences;
        mEditor = mPreferences.edit();
    }

    //region GeoSharedPrefs
    @Override
    public void add(String key, String entity) {
        mEditor.putString(key, entity);
    }

    @Override
    public void add(String key, float entity) {
        mEditor.putFloat(key, entity);
    }


    public void add(String key, int entity){
        mEditor.putInt(key, entity);
    }

    @Override
    public void remove(String key) {
        mEditor.remove(key);
    }

    @Override
    public void commit() {
        mEditor.commit();
    }

    @Override
    public void apply() {
        mEditor.apply();
    }
    //endregion

    //region SharedPrefs
    @Override
    public Map<String, ?> getAll() {
        return mPreferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mPreferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mPreferences.contains(key);
    }

    @Override
    public Editor edit() {
        return mPreferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
    //endregion
}
