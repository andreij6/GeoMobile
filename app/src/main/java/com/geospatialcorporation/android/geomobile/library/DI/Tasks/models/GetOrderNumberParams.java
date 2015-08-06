package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetOrderNumberParams {
    private Layer mLayer;
    private CheckBox mCheckBox;
    private Spinner mColumnTypes;
    private EditText mName;
    private EditText mDefault;
    private IPostExecuter mExecuter;

    public GetOrderNumberParams(Layer layer, CheckBox isHidden, Spinner columnTypes, EditText nameET, EditText defaultValueET, IPostExecuter executer) {
        mLayer = layer;
        mCheckBox = isHidden;
        mColumnTypes = columnTypes;
        mName = nameET;
        mDefault = defaultValueET;
        mExecuter = executer;
    }

    public Layer getLayer() {
        return mLayer;
    }

    public IPostExecuter getExecuter(){
        return mExecuter;
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
