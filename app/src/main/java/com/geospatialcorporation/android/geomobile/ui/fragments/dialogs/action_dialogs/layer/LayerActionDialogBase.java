package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.content.Context;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class LayerActionDialogBase extends GeoDialogFragmentBase {
    protected Layer mLayer;
    protected ILayerTreeService mService;

    protected static final String LAYER_DIALOG = "layer_dialog";

    public void init(Layer layer){
        setContext(getActivity());
        mLayer = layer;
        mService = application.getTreeServiceComponent().provideLayerTreeService();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYER_DIALOG, mLayer);
    }
}
