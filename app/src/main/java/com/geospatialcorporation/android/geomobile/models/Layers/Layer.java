package com.geospatialcorporation.android.geomobile.models.Layers;

public class Layer {
    //region Properties
    private Extent Extent;
    private String StylePath;
    private Integer GeometryTypeCodeId;
    private Boolean IsFixed;
    private Boolean IsOwner;
    private Integer Id;
    private Integer MobileId;
    private String Name;
    private Boolean IsShowing;
    //endregion

    //region Getters & Setters
    public Boolean getIsShowing() {
        return IsShowing;
    }

    public void setIsShowing(Boolean isShowing) {
        IsShowing = isShowing;
    }

    public Extent getExtent() {
        return Extent;
    }

    public void setExtent(Extent extent) {
        Extent = extent;
    }

    public String getStylePath() {
        return StylePath;
    }

    public void setStylePath(String stylePath) {
        StylePath = stylePath;
    }

    public Integer getGeometryTypeCodeId() {
        return GeometryTypeCodeId;
    }

    public void setGeometryTypeCodeId(Integer geometryTypeCodeId) {
        GeometryTypeCodeId = geometryTypeCodeId;
    }

    public Boolean getIsFixed() {
        return IsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        IsFixed = isFixed;
    }

    public Boolean getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        IsOwner = isOwner;
    }

    public Integer getMobileId() {
        return MobileId;
    }

    public void setMobileId(Integer id) {
        MobileId = id;
    }

    public Integer getId() { return Id; }

    public void setId(Integer id){ Id = id; }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion

    //region Constructors
    public Layer(String name) { Name = name; }
    public Layer(){}
    //endregion

}