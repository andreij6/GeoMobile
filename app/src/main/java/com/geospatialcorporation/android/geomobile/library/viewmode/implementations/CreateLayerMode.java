package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;

/**
 * Created by andre on 6/6/2015.
 */
public class CreateLayerMode implements IViewMode {

    @Override
    public void Disable(Boolean showPanel) {

    }

    @Override
    public boolean isSame(IViewMode mode) {
        return mode instanceof CreateLayerMode;
    }
}
