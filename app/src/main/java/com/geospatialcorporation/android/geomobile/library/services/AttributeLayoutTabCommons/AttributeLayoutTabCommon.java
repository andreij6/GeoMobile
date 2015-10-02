package com.geospatialcorporation.android.geomobile.library.services.AttributeLayoutTabCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet.TabletAttributeLayoutTab;

public class AttributeLayoutTabCommon implements IAttributeLayoutTabCommon {
    @Override
    public void swipe(SwipeRefreshLayout swipeRefreshLayout, IContentRefresher contentRefresher, Resources resources) {
        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        swipeRefreshLayout.setOnRefreshListener(refresher.build(swipeRefreshLayout, contentRefresher));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.accent));

    }

    @Override
    public Layer handleArguments(Bundle arguments) {
        return arguments.getParcelable(Layer.LAYER_INTENT);
    }
}
