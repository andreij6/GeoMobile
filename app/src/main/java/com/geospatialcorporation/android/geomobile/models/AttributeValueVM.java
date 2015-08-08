package com.geospatialcorporation.android.geomobile.models;

import java.util.List;

public class AttributeValueVM {

    Integer mLayerId;
    List<Columns> mColumns;

    public AttributeValueVM(Integer layerId, List<Columns> columns){
        mLayerId = layerId;
        mColumns = columns;
    }

    public void setColumns(List<Columns> columns) {
        mColumns = columns;
    }

    public List<Columns> getColumns() {
        return mColumns;
    }

    public int getLayerId(){
        return mLayerId;
    }

    public static class Columns {

        String mKey;
        String mValue;
        Integer mColumnId;
        String mFeatureId;
        Integer mDataType;

        public Columns(String key, String value, Integer columnId, String featureId, Integer dataType) {
            mKey = key;
            mValue = value;
            mColumnId = columnId;
            mFeatureId = featureId;
            mDataType = dataType;
        }

        public String getKey() {
            return mKey;
        }

        public void setKey(String key) {
            mKey = key;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String value) {
            mValue = value;
        }

        public Integer getColumnId() {
            return mColumnId;
        }

        public void setColumnId(Integer columnId) {
            mColumnId = columnId;
        }

        public String getFeatureId() {
            return mFeatureId;
        }

        public void setFeatureId(String featureId) {
            mFeatureId = featureId;
        }

        public void setFeature(int dataType){
            mDataType = dataType;
        }

        public Integer getDataType(){
            return mDataType;
        }
    }
}
