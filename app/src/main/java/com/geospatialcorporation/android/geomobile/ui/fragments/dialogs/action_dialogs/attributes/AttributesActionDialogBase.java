package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class AttributesActionDialogBase<T> extends GeoDialogFragmentBase {

    T mData;

    public void init(Context context, T data){
        setContext(context);
        mData = data;
        //mService = application.getTreeServiceComponent().provideLayerTreeService();
    }

}
