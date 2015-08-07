package com.geospatialcorporation.android.geomobile.models.Layers;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/25/2015.
 */
public class LegendLayer {
    Layer mLayer;
    Drawable mLegendIcon;
    List<Object> MapObjects;
    ImageView mImageView;
    Folder mFolder;
    private ProgressBar mProgressBar;
    private CheckBox mCheckBox;

    public LegendLayer(Layer layer) {
        mLayer = layer;
        MapObjects = new ArrayList<>();
    }

    public LegendLayer(Folder folder) {
        mFolder = folder;
    }

    //region Gs & Ss
    public Layer getLayer() {
        return mLayer;
    }

    public void setLayer(Layer layer) {
        mLayer = layer;
    }

    public Drawable getLegendIcon() {
        return mLegendIcon;
    }

    public void setLegendIcon(Drawable legendIcon) {
        mLegendIcon = legendIcon;
    }

    public List<Object> getMapObject() {
        return MapObjects;
    }

    public void setMapObject(Object mapObject) {
        MapObjects.add(mapObject);
    }
    //endregion

    public void clearMapFeatures() {

        for(Object mapObject : MapObjects){
            if(mapObject instanceof Marker){
                ((Marker) mapObject).remove();
            }
            if(mapObject instanceof Polyline){
                ((Polyline) mapObject).remove();
            }
            if(mapObject instanceof Polygon){
                ((Polygon) mapObject).remove();
            }
        }
    }

    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }

    public void setImageSrc(){
        mImageView.setImageDrawable(mLegendIcon);
    }

    public boolean isIconSet() {
        return mLayer.getIsShowing();
    }

    public Folder getFolder() {
        return mFolder;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setCheckBox(CheckBox checkBox) {
        mCheckBox = checkBox;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }
}
