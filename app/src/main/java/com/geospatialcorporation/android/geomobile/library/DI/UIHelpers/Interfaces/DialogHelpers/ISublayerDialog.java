package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

public interface ISublayerDialog extends ICreatorDialog<Layer, Layer> {
    void modify(Layer layer, Context context, FragmentManager manager);
}
