package com.geospatialcorporation.android.geomobile.models.Layers;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.geospatialcorporation.android.geomobile.library.map.MapActions;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;

import java.util.List;

public class Layer implements Parcelable, ITreeEntity {

    //region Constructors
    public Layer(String name) {
        Name = name;
        IsShowing = false;
    }

    public Layer(){
        IsShowing = false;
    }

    private Layer(Parcel in){
        Id = in.readInt();
        //Extent = in.;
        StylePath = in.readString();
        GeometryTypeCodeId = in.readInt();
        IsFixed = (Boolean)in.readValue(Boolean.class.getClassLoader());
        IsOwner = (Boolean)in.readValue(Boolean.class.getClassLoader());
        //MobileId = in.readInt();
        Name = in.readString();
        IsShowing = (Boolean)in.readValue(Boolean.class.getClassLoader());
    }
    //endregion

    //region Properties
    private Integer Id;
    private Extent Extent;
    private String StylePath;
    private Integer GeometryTypeCodeId;
    private Boolean IsFixed;
    private Boolean IsOwner;
    private Integer MobileId;
    private String Name;
    private Boolean IsShowing;
    private List<Layer> Sublayers;
    private StyleInfo StyleInfo;
    private Object MapObject;
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
        new MapActions().showLayer(this);
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

    public void setMapObject(Object mapObject) { MapObject = mapObject; }
    public Object getMapObject() { return MapObject; }
    //endregion

    public static String LAYER_INTENT = "Layer Intent";

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