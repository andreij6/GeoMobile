package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;


public abstract class GeoDetailsTabBase<T> extends GeoViewFragmentBase {
    private static final String TAG = GeoDetailsTabBase.class.getSimpleName();

    protected ITreeService mService;
    protected T mEntity;

    public String getIntentString() {
        return mIntentString;
    }

    public void setIntentString(String intentString) {
        mIntentString = intentString;
    }

    String mIntentString;

    public void handleArgs(){
        Bundle args = getArguments();

        mEntity = args.getParcelable(getIntentString());
    }


    public abstract void refresh();

}
