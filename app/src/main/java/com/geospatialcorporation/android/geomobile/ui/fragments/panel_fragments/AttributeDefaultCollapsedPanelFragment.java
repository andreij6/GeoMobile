package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetOrderNumberTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetOrderNumberParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.validation.requests.IValidateRequest;
import com.geospatialcorporation.android.geomobile.library.validation.requests.LayerAttributeValidateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class AttributeDefaultCollapsedPanelFragment extends GeoViewFragmentBase {

    ILayerTreeService mService;
    Layer mLayer;
    IGetOrderNumberTask mTask;

    //region Butterknife
    @InjectView(R.id.create) Button mCreateBtn;
    @InjectView(R.id.nameET) EditText mNameET;
    @InjectView(R.id.defaultValueET) EditText mDefaultValueET;
    @InjectView(R.id.columnTypes) Spinner mColumnTypes;
    @InjectView(R.id.isHiddenCB) CheckBox mIsHidden;
    @InjectView(R.id.toggle) Button mToggle;

    @OnClick(R.id.create)
    public void createLayerAttribute(){
        if(Valid()){
            mTask.getOrderNumber(new GetOrderNumberParams(mLayer, this, mIsHidden, mColumnTypes, mNameET, mDefaultValueET));
        }
    }

    @OnClick(R.id.toggle)
    public void toggle(){
        mPanelManager.toggle();
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_panel_attribute_collapsed, container, false);
        ButterKnife.inject(this, mView);

        mTask = application.getTasksComponent().provideOrderNumberTask();

        mPanelManager = new PanelManager(GeoPanel.LAYER_ATTRIBUTE);
        mPanelManager.touch(false);

        handleArgs();

        setupSpinner();

        mService = application.getTreeServiceComponent().provideLayerTreeService();

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mPanelManager.collapse();
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mLayer = args.getParcelable(Layer.LAYER_INTENT);
    }

    protected void setupSpinner() {

        String[] choices = getResources().getStringArray(R.array.layer_attribute_column_types);

        ArrayAdapter<String> a = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, choices);

        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mColumnTypes.setAdapter(a);

    }

    protected boolean Valid() {
        IValidateRequest request = new LayerAttributeValidateRequest(getValue(mNameET), getValue(mDefaultValueET), getValue(mColumnTypes));

        return request.isValid();
    }

}
