package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FeatureWindowData {

    private HashMap<String, String> data;
    private List<KeyValue> mList;

    public FeatureWindowData(){
        data = new HashMap<>();
        mList = new ArrayList<>();
    }

    public void addEntry(String key, String value) {
        data.put(key, value);
    }

    public List<KeyValue> getList() {
        for(String key : data.keySet()){
            mList.add(new KeyValue(key, data.get(key)));
        }
        Collections.reverse(mList);

        return mList;
    }

    public static class KeyValue{
        public String mKey;
        public String mValue;

        public KeyValue(String key, String value){
            mKey = key;
            mValue = value;
        }


        public String getKey() {
            return mKey;
        }

        public String getValue() {
            return mValue;
        }
    }
}
