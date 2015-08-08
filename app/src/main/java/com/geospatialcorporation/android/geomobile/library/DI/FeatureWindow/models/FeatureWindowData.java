package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FeatureWindowData {

    private List<KeyValue> mList;

    public FeatureWindowData(){
        mList = new ArrayList<>();
    }

    public void addEntry(String key, String value, Integer order) {
        mList.add(new KeyValue(key, value, order));
    }

    public List<KeyValue> getList() {
        return mList;
    }

    public static class KeyValue implements Comparable<KeyValue> {
        public String mKey;
        public String mValue;
        public Integer mOrder;

        public KeyValue(String key, String value, int order){
            mKey = key;
            mValue = value;
            mOrder = order;
        }


        public String getKey() {
            return mKey;
        }

        public String getValue() {
            return mValue;
        }

        public Integer getOrder(){return mOrder; }

        @Override
        public int compareTo(KeyValue another) {
            return this.mOrder - another.getOrder();
        }
    }
}
