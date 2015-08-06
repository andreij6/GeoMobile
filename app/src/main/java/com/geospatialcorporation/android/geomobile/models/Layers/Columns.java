package com.geospatialcorporation.android.geomobile.models.Layers;

import java.util.List;

/**
 * Created by andre on 6/19/2015.
 */
public class Columns {

    //region Getter & Setters
    public Integer getIndex() {
        return Index;
    }

    public void setIndex(Integer index) {
        Index = index;
    }

    public Integer getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(Integer orderNum) {
        OrderNum = orderNum;
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

    Integer Id;
    Integer Index;
    String Name;
    Integer DataType;
    Boolean IsHidden;
    String DefaultValue;
    Integer OrderNum;

    public Columns(Integer index, LayerAttributeColumn lac){
        Index = index;
        OrderNum = lac.getOrderNum();
        Id = -1;
        Name = lac.getName();
        DataType = lac.getDataType();
        IsHidden = lac.getIsHidden();
        DefaultValue = lac.getDefaultValue();
    }
}
