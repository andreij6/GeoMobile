package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import java.util.List;

public class AttributesActionDialogBase extends GeoDialogFragmentBase {

    AttributeValueVM mData;

    public void init(Context context, AttributeValueVM data){
        setContext(context);
        mData = data;
        //mService = application.getTreeServiceComponent().provideLayerTreeService();
    }

}
