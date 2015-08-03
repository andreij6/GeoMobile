package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetOrderNumberTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetOrderNumberParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import java.util.ArrayList;
import java.util.List;

public class GetOrderNumberTask implements IGetOrderNumberTask {

    ILayerTreeService mLayerTreeService;

    public GetOrderNumberTask(){

        mLayerTreeService = application.getTreeServiceComponent().provideLayerTreeService();
    }

    @Override
    public void getOrderNumber(GetOrderNumberParams params) {
        new GetOrderNumberAsync(params).execute();
    }

    //TODO: gEOaSYNC
    protected class GetOrderNumberAsync extends AsyncTask<Void, Void, Integer> {

        Layer mLayer;
        GeoViewFragmentBase mViewFragmentBase;
        CheckBox mIsHidden;
        Spinner mColumnTypes;
        EditText mNameET;
        EditText mDefaultValueET;


        public GetOrderNumberAsync(GetOrderNumberParams params){
            mLayer = params.getLayer();
            mViewFragmentBase = params.getViewFragment();
            mIsHidden = params.getCheckBox();
            mColumnTypes = params.getColumnTypes();
            mNameET = params.getName();
            mDefaultValueET = params.getDefault();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            List<LayerAttributeColumn> columns = new ArrayList<>();

            if(mLayer != null){
                columns = mLayerTreeService.getColumns(mLayer.getId());
            }

            return columns.size();
        }

        @Override
        protected void onPostExecute(Integer orderNum) {
            LayerAttributeColumn newColumn = new LayerAttributeColumn();

            //TODO: 400 Bad Request Error
            newColumn.setId(-1);
            newColumn.setDataType(mViewFragmentBase.getValue(mColumnTypes));
            newColumn.setName(mViewFragmentBase.getValue(mNameET));
            newColumn.setIsHidden(mIsHidden.isChecked());
            newColumn.setDefaultValue(mViewFragmentBase.getValue(mDefaultValueET));
            newColumn.setOrderNum(orderNum + 1);

            Columns vm = new Columns(orderNum, newColumn);

            mLayerTreeService.addColumn(mLayer.getId(), vm);
        }


    }
}
