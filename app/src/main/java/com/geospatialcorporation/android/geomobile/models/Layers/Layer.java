package com.geospatialcorporation.android.geomobile.models.Layers;

/**
 * Created by andre on 4/7/2015.
 */
public class Layer {
    //region Properties
    private Extent mExtent;
    private String mStylePath;
    private Integer mGeometryTypeCodeId;
    private Boolean mIsFixed;
    private Boolean mIsOwner;
    private Integer mId;
    private String mName;
    private Boolean mIsShowing;
    //endregion

    //region Getters & Setters
    public Boolean getIsShowing() {
        return mIsShowing;
    }

    public void setIsShowing(Boolean isShowing) {
        mIsShowing = isShowing;
    }

    public Extent getExtent() {
        return mExtent;
    }

    public void setExtent(Extent extent) {
        mExtent = extent;
    }

    public String getStylePath() {
        return mStylePath;
    }

    public void setStylePath(String stylePath) {
        mStylePath = stylePath;
    }

    public Integer getGeometryTypeCodeId() {
        return mGeometryTypeCodeId;
    }

    public void setGeometryTypeCodeId(Integer geometryTypeCodeId) {
        mGeometryTypeCodeId = geometryTypeCodeId;
    }

    public Boolean getIsFixed() {
        return mIsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        mIsFixed = isFixed;
    }

    public Boolean getIsOwner() {
        return mIsOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        mIsOwner = isOwner;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
    //endregion

}
