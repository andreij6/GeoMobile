package com.geospatialcorporation.android.geomobile.models.Layers;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.constants.PluginCodes;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;

import java.util.List;

public class Layer implements Parcelable, ITreeEntity {

    //region Constructors
    public Layer(String name) {
        Name = name;
        IsShowing = false;
        PluginId = 0;
    }

    public Layer(){
        PluginId = 0;
        IsShowing = false;
    }

    private Layer(Parcel in){
        Id = in.readInt();
        //Extent = in.;
        StylePath = in.readString();
        GeometryTypeCodeId = in.readInt();
        IsFixed = (Boolean)in.readValue(Boolean.class.getClassLoader());
        IsOwner = (Boolean)in.readValue(Boolean.class.getClassLoader());
        Name = in.readString();
        IsShowing = (Boolean)in.readValue(Boolean.class.getClassLoader());
        PluginId = in.readInt();
    }
    //endregion

    //region Properties
    private Integer Id;
    private Extent Extent;
    private String StylePath;
    private Integer GeometryTypeCodeId;
    private Boolean IsFixed;
    private Boolean IsOwner;
    private String Name;
    private Boolean IsShowing;
    private List<Layer> Sublayers;
    private StyleInfo StyleInfo;
    private int PluginId;
    //endregion

    //region Getters & Setters
    public int getPluginId() {
        return PluginId;
    }

    public void setPluginId(int pluginId) {
        PluginId = pluginId;
    }

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

    public int getId() { return Id; }

    public void setId(Integer id){ Id = id; }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSublayers(List<Layer> sublayers) {
        Sublayers = sublayers;
    }
    
    public List<Layer> getSublayers() {
        return Sublayers;
    }

    public void setStyleInfo(StyleInfo styleInfo) {
        StyleInfo = styleInfo;
    }

    public StyleInfo getStyleInfo() {
        return StyleInfo;
    }
    //endregion

    public static String LAYER_INTENT = "Layer Intent";

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);

        dest.writeString(StylePath);
        dest.writeInt(GeometryTypeCodeId);
        dest.writeValue(IsFixed);
        dest.writeValue(IsOwner);

        dest.writeString(Name);
        dest.writeValue(IsShowing);

        dest.writeInt(PluginId);
    }

    public static final Creator<Layer> CREATOR = new Creator<Layer>(){

        @Override
        public Layer createFromParcel(Parcel source) {
            return new Layer(source);
        }

        @Override
        public Layer[] newArray(int size) {
            return new Layer[size];
        }
    };
    //endregion

    public String getReadableGeometryType() {
        int code = getGeometryTypeCodeId();
        String result = "";
        switch(code){
            case GeometryTypeCodes.Point:
                result  = "Point";
                break;
            case GeometryTypeCodes.Line:
                result  = "Line";
                break;
            case GeometryTypeCodes.Polygon:
                result  = "Polygon";
                break;
            case GeometryTypeCodes.MultiPoint:
                result  = "MultiPoint";
                break;
            case GeometryTypeCodes.MultiLine:
                result  = "MultiLine";
                break;
            case GeometryTypeCodes.MultiPolygon:
                result  = "MultiPolygon";
                break;
            case GeometryTypeCodes.Collection:
                result  = "Collection";
                break;
            case GeometryTypeCodes.Extent:
                result  = "Extent";
                break;
            case GeometryTypeCodes.Raster:
                result  = "Raster";
                break;
        }

        return result;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putParcelable(LAYER_INTENT, this);
        return b;
    }

    public boolean isMobileCompatible() {
        return PluginId == PluginCodes.Default; // || PluginId == PluginCodes.VideoLayers;
    }

    public class StyleInfo {
        public int StyleInfoId;
        public String Name;
        public int PointStyleCode;
        public int LineStyleCode;
        public int Width;
        public int BorderWidth;
        public String BorderColor;
        public String FillColor;

        public boolean IsBorderVisible() {
            return TextUtils.isEmpty(BorderColor) || BorderColor.substring(6, 2).equals("00");
        }

        public boolean IsFillVisible() {
            return TextUtils.isEmpty(FillColor) || FillColor.substring(6, 2).equals("00");
        }

        public StyleInfo() {}
    }
}