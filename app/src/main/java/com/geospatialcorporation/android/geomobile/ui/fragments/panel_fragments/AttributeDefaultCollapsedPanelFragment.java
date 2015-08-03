package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.AsyncTask;
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
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.validation.requests.IValidateRequest;
import com.geospatialcorporation.android.geomobile.library.validation.requests.LayerAttributeValidateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/19/2015.
 */
public class AttributeDefaultCollapsedPanelFragment extends GeoViewFragmentBase {

    LayerTreeService mService;
    Layer mLayer;

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
            new GetOrderNumberTask().execute();
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

        mPanelManager = new PanelManager(GeoPanel.LAYER_ATTRIBUTE);
        mPanelManager.touch(false);

        handleArgs();

        setupSpinner();

        mService = new LayerTreeService();

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

    protected int getOrderNumber() {
        List<LayerAttributeColumn> columns = mService.getLayerAttributeColumns(mLayer.getId());

        return columns.size();
    }

    private class GetOrderNumberTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            mService = new LayerTreeService();
            List<LayerAttributeColumn> columns = new ArrayList<>();

            if(mLayer != null){
                columns = mService.getLayerAttributeColumns(mLayer.getId());
            }

            return columns.size();
        }

        @Override
        protected void onPostExecute(Integer orderNum) {
            LayerAttributeColumn newColumn = new LayerAttributeColumn();

            //TODO: 400 Bad Request Error
            newColumn.setId(-1);
            newColumn.setDataType(getValue(mColumnTypes));
            newColumn.setName(getValue(mNameET));
            newColumn.setIsHidden(mIsHidden.isChecked());
            newColumn.setDefaultValue(getValue(mDefaultValueET));
            newColumn.setOrderNum(orderNum + 1);

            Columns vm = new Columns(orderNum, newColumn);

            mService.addLayerAttributeColumn(mLayer.getId(), vm);
        }


    }

}
