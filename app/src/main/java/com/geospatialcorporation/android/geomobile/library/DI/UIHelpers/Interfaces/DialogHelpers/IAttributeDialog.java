package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

public interface IAttributeDialog {
    void edit(AttributeValueVM data, Context context, FragmentManager manager);

    void create(Layer entity, Context context, FragmentManager fragmentManager);
}
