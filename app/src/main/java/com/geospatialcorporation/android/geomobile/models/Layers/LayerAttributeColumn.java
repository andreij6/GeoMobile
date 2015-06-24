package com.geospatialcorporation.android.geomobile.models.Layers;

import com.geospatialcorporation.android.geomobile.library.constants.LayerAttributeColumnDataTypeCodes;

import java.util.Objects;


public class LayerAttributeColumn {
    //region GETTERS & SETTERS
    public Integer getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(Integer orderNum) {
        OrderNum = orderNum;
    }

    public Boolean getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        IsDeleted = isDeleted;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getDataType() {
        return DataType;
    }

    public String getDataTypeViewName() {
        String result = "Missing Data Type";

        //switch wouldnt work right here IDK y.
        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.IDFIELD)) {
            result = "Id Field";
        }
        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.TEXT)) {
            result = "Text";
        }

        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.BOOLEAN)){
            result = "True / False";
        }

        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.DATE)){
            result = "Date";
        }
        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.DATETIME)) {
            result = "DateTime";
        }
        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.INTEGER)) {
            result = "Integer";
        }
        if (Objects.equals(getDataType(), LayerAttributeColumnDataTypeCodes.DECIMAL)) {
            result = "Decimal";
        }

        return result;
    }

    public void setDataType(Integer dataType) {
        DataType = dataType;
    }

    public void setDataType(String dataType){

        switch (dataType){
            case "Text":
                setDataType(LayerAttributeColumnDataTypeCodes.TEXT);
                break;
            case "True/False":
                setDataType(LayerAttributeColumnDataTypeCodes.BOOLEAN);
                break;
            case "Date":
                setDataType(LayerAttributeColumnDataTypeCodes.DATE);
                break;
            case "DateTime":
                setDataType(LayerAttributeColumnDataTypeCodes.DATETIME);
                break;
            case "Integer":
                setDataType(LayerAttributeColumnDataTypeCodes.INTEGER);
                break;
            case "Decimal":
                setDataType(LayerAttributeColumnDataTypeCodes.DECIMAL);
                break;

        }
    }

    public Boolean getIsHidden() {
        return IsHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        IsHidden = isHidden;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        DefaultValue = defaultValue;
    }

    //endregion

    Integer OrderNum;
    Boolean IsDeleted;
    Integer Id;
    String Name;
    Integer DataType;
    Boolean IsHidden;
    String DefaultValue;


}
