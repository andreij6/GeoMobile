package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
public class SublayerActionDialogFragmentBase extends GeoDialogFragmentBase {
    protected Layer mEntity;
    protected ISublayerTreeService mService;

    public void init(Context context, Layer entity){
        setContext(context);
        mEntity = entity;
        mService = application.getTreeServiceComponent().provideSublayerTreeService();
    }

}
