package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
public class LayerActionDialogBase extends GeoDialogFragmentBase {
    protected Layer mLayer;
    protected ILayerTreeService mService;

    public void init(Context context, Layer layer){
        setContext(context);
        mLayer = layer;
        mService = application.getTreeServiceComponent().provideLayerTreeService();
    }
}
