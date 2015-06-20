package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

/**
 * Created by andre on 6/9/2015.
 */
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
