package com.geospatialcorporation.android.geomobile.library.services.AttributeLayoutTabCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;

public interface IAttributeLayoutTabCommon {
    void swipe(SwipeRefreshLayout swipeRefreshLayout, IContentRefresher contentRefresher, Resources resources);

    Layer handleArguments(Bundle arguments);

}
