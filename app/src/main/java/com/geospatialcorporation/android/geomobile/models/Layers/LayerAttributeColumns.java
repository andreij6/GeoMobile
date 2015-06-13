package com.geospatialcorporation.android.geomobile.models.Layers;

import com.geospatialcorporation.android.geomobile.library.constants.LayerAttributeColumnDataTypeCodes;


/**
 * Created by andre on 6/9/2015.
 */
public class LayerAttributeColumns {
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

    public String getDataTypeName() {
        String result = "Missing Data Type";

        //switch wouldnt work right here IDK y.
        if (getDataType() == LayerAttributeColumnDataTypeCodes.IDFIELD) {
            result = "IdField";
        }
        if (getDataType() == LayerAttributeColumnDataTypeCodes.TEXT) {
            result = "Text";
        }

        if (getDataType() == LayerAttributeColumnDataTypeCodes.BOOLEAN){
            result = "Boolean";
        }

        if (getDataType() == LayerAttributeColumnDataTypeCodes.DATE){
            result = "Date";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.DATETIME) {
            result = "DateTime";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.INTEGER) {
            result = "Integer";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.DECIMAL) {
            result = "Decimal";
        }

        return result;
    }

    public String getDataTypeViewName() {
        String result = "Missing Data Type";

        //switch wouldnt work right here IDK y.
        if (getDataType() == LayerAttributeColumnDataTypeCodes.IDFIELD) {
            result = "Id Field";
        }
        if (getDataType() == LayerAttributeColumnDataTypeCodes.TEXT) {
            result = "Text";
        }

        if (getDataType() == LayerAttributeColumnDataTypeCodes.BOOLEAN){
            result = "True / False";
        }

        if (getDataType() == LayerAttributeColumnDataTypeCodes.DATE){
            result = "Date";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.DATETIME) {
            result = "DateTime";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.INTEGER) {
            result = "Integer";
        }
        if (getDataType() ==LayerAttributeColumnDataTypeCodes.DECIMAL) {
            result = "Decimal";
        }

        return result;
    }

    public void setDataType(Integer dataType) {
        DataType = dataType;
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
