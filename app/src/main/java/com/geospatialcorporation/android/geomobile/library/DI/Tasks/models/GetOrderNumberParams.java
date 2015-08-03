package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.AttributeDefaultCollapsedPanelFragment;

public class GetOrderNumberParams {
    private Layer mLayer;
    private GeoViewFragmentBase mViewFragment;
    private CheckBox mCheckBox;
    private Spinner mColumnTypes;
    private EditText mName;
    private EditText mDefault;

    public GetOrderNumberParams(Layer layer, AttributeDefaultCollapsedPanelFragment attributeDefaultCollapsedPanelFragment, CheckBox isHidden, Spinner columnTypes, EditText nameET, EditText defaultValueET) {
        mLayer = layer;
        mViewFragment = attributeDefaultCollapsedPanelFragment;
        mCheckBox = isHidden;
        mColumnTypes = columnTypes;
        mName = nameET;
        mDefault = defaultValueET;
    }

    public Layer getLayer() {
        return mLayer;
    }

    public GeoViewFragmentBase getViewFragment() {
        return mViewFragment;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public Spinner getColumnTypes() {
        return mColumnTypes;
    }

    public EditText getName() {
        return mName;
    }

    public EditText getDefault() {
        return mDefault;
    }
}
