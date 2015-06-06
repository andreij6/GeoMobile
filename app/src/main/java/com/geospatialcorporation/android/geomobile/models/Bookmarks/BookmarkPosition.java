package com.geospatialcorporation.android.geomobile.models.Bookmarks;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkPosition {
    private float mBearing;
    private float mTilt;
    private float mLat;
    private float mZoom;
    private float mLng;

    public void setBearing(float bearing) {
        mBearing = bearing;
    }

    public float getBearing() {
        return mBearing;
    }

    public void setTilt(float tilt) {
        mTilt = tilt;
    }

    public float getTilt() {
        return mTilt;
    }

    public void setLat(float lat) {
        mLat = lat;
    }

    public float getLat(){
        return mLat;
    }

    public void setZoom(float zoom) {
        mZoom = zoom;
    }

    public float getZoom() {
        return mZoom;
    }

    public void setLng(float lng) {
        mLng = lng;
    }

    public float getLng() {
        return mLng;
    }
}
