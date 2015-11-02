package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.ILayerEditor;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPolygonPanelFragment extends GeoViewFragmentBase {

    ILayerEditor mEditor;
    @Bind(R.id.task) TextView mTaskTitle;

    @OnClick(R.id.create_polygon)
    public void createPolygon(){
        mTaskTitle.setText(getString(R.string.add_polygon));

        mEditor.addClickListener();
    }

    @OnClick(R.id.remove_polygon)
    public void removePolygon(){
        mTaskTitle.setText(getString(R.string.remove_polygon));

        mEditor.removeClickListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_panel_edit_polygon, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    public void init(ILayerEditor editor) {
        mEditor = editor;
    }
}
