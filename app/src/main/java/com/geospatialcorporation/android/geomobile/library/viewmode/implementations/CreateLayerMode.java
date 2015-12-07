package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;

public class CreateLayerMode implements IViewMode {

    @Override
    public void Disable(Boolean showPanel) {

    }

    @Override
    public boolean isSame(IViewMode mode) {
        return mode instanceof CreateLayerMode;
    }
}
