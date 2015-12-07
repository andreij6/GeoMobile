package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

public interface IAttributeDialog {
    void edit(AttributeValueVM data, Context context, FragmentManager manager);

    void create(Layer entity, Context context, FragmentManager fragmentManager);
}
