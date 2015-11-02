package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.ILayerEditor;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPointPanelFragment extends GeoViewFragmentBase {

    ILayerEditor mEditor;
    @Bind(R.id.add_point) ImageButton mAddPoint;
    @Bind(R.id.move_point) ImageButton mMovePoint;
    @Bind(R.id.remove_point) ImageButton mErase;
    @Bind(R.id.task) TextView mTaskTitle;

    @OnClick(R.id.add_point)
    public void addPoint(){
        mTaskTitle.setText(getString(R.string.add_point));

        mEditor.addClickListener();
    }

    @OnClick(R.id.move_point)
    public void movePoint(){
        mTaskTitle.setText(getString(R.string.move_point));

        mEditor.moveClickListener();
    }

    @OnClick(R.id.remove_point)
    public void removePoint(){
        mTaskTitle.setText(getString(R.string.remove_point));

        mEditor.removeClickListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_panel_edit_point, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    public void init(ILayerEditor editor) {
        mEditor = editor;
    }
}
