package com.geospatialcorporation.android.geomobile.models.Layers;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.google.android.gms.maps.model.BitmapDescriptor;

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
    private boolean mMapped;
    private BitmapDescriptor mBitmap;

    public LegendLayer(Layer layer) {
        mLayer = layer;
        MapObjects = new ArrayList<>();
        mMapped = false;
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
    //endregion

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

    public boolean isMapped() {
        return mMapped;
    }

    public void setMapped(boolean mapped) {
        mMapped = mapped;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public BitmapDescriptor getBitmap() {
        return mBitmap;
    }

    public void setBitmap(BitmapDescriptor bitmap) {
        mBitmap = bitmap;
    }

    public boolean isCheckBoxEnabled() {
        if(mCheckBox != null){
            return mCheckBox.isEnabled();
        } else {
            return true;
        }
    }
}
