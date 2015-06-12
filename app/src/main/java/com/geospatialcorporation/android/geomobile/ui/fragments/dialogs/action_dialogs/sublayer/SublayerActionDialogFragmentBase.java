package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.services.SublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
public class SublayerActionDialogFragmentBase extends GeoDialogFragmentBase {
    protected Layer mSublayer;
    protected SublayerTreeService mService;

    public void init(Context context, Layer sublayer){
        setContext(context);
        mSublayer = sublayer;
        mService = new SublayerTreeService();
    }

}
