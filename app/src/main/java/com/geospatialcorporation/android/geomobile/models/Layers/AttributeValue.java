package com.geospatialcorporation.android.geomobile.models.Layers;

public class AttributeValue {
    int AttributeColumnId;
    String Value;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public int getAttributeColumnId() {
        return AttributeColumnId;
    }

    public void setAttributeColumnId(int attributeColumnId) {
        AttributeColumnId = attributeColumnId;
    }
}
