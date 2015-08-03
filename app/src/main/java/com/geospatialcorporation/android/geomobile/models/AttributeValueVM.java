package com.geospatialcorporation.android.geomobile.models;

import java.util.List;

public class AttributeValueVM {

    List<Columns> mColumns;

    public void setColumns(List<Columns> columns) {
        mColumns = columns;
    }

    public List<Columns> getColumns() {
        return mColumns;
    }

    public static class Columns {

        String mKey;
        String mValue;
        Integer mColumnId;
        String mFeatureId;

        public Columns(String key, String value, Integer columnId, String featureId) {
            mKey = key;
            mValue = value;
            mColumnId = columnId;
            mFeatureId = featureId;
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
    }
}
