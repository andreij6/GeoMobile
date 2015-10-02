package com.geospatialcorporation.android.geomobile.library.services.SublayerTabCommons;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.widget.TableLayout;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetSublayersTaskParams;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;

import java.util.List;

public interface ISublayerTabCommon {
    void swipe(SwipeRefreshLayout swipeRefreshLayout, IContentRefresher contentRefresher, Resources resources);


    void onPostExecute(List<Layer> model, TableLayout sublayerDataView, LayoutInflater inflater, Context context);

    void getSublayersAsync(GetSublayersTaskParams getSublayersTaskParams);

    Layer handleArgugments(Bundle arguments);
}
