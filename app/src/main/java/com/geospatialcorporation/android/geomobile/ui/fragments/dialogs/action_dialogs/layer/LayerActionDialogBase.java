package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
public class LayerActionDialogBase extends GeoDialogFragmentBase {
    Layer mLayer;
    LayerTreeService mService;

    public void init(Context context, Layer layer){
        setContext(context);
        mLayer = layer;
        mService = new LayerTreeService();
    }
}
