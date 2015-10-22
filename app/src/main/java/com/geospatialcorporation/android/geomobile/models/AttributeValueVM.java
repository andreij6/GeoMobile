package com.geospatialcorporation.android.geomobile.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

public class AttributeValueVM implements Parcelable {

    Integer mLayerId;
    List<Columns> mColumns;
    public static final String ATTRIBUTE_VALUE = "AttributeValue";

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

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putParcelable(ATTRIBUTE_VALUE, this);
        return b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLayerId);
        dest.writeTypedList(mColumns);
    }

    private AttributeValueVM(Parcel in){
        mLayerId = in.readInt();
        mColumns = new ArrayList<>();
        in.readTypedList(mColumns, Columns.CREATOR);
    }

    public static final Creator<AttributeValueVM> CREATOR = new Creator<AttributeValueVM>(){

        @Override
        public AttributeValueVM createFromParcel(Parcel source) {
            return new AttributeValueVM(source);
        }

        @Override
        public AttributeValueVM[] newArray(int size) {
            return new AttributeValueVM[size];
        }
    };

    public static class Columns implements Parcelable {

        String mKey;
        String mValue;
        Integer mColumnId;
        String mFeatureId;
        Integer mDataType;
        Boolean mEditable;

        public Columns(String key, String value, Integer columnId, String featureId, Integer dataType, Boolean editable) {
            mKey = key;
            mValue = value;
            mColumnId = columnId;
            mFeatureId = featureId;
            mDataType = dataType;
            mEditable = editable;
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

        public boolean isEditable() {
            return mEditable;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mKey);
            dest.writeString(mValue);
            dest.writeString(mFeatureId);
            dest.writeInt(mColumnId);
            dest.writeInt(mDataType);
            dest.writeValue(mEditable);
        }

        private Columns(Parcel in){
            mKey = in.readString();
            mValue = in.readString();
            mFeatureId = in.readString();
            mColumnId = in.readInt();
            mDataType = in.readInt();
            mEditable = (Boolean)in.readValue(Boolean.class.getClassLoader());
        }

        public static final Creator<Columns> CREATOR = new Creator<Columns>(){

            @Override
            public Columns createFromParcel(Parcel source) {
                return new Columns(source);
            }

            @Override
            public Columns[] newArray(int size) {
                return new Columns[size];
            }
        };
    }
}
