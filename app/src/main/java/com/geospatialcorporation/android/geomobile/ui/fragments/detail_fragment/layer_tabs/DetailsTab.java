package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerDetailParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DetailsTab extends GeoDetailsTabBase<Layer> implements IPostExecuter<LayerDetailsVm> {

    //region Properties ButterKnife
    LayerDetailsVm mDetails;
    IGetLayerDetailsTask mTask;
    ILayerDialog mLayerDialog;

    @InjectView(R.id.access_level) TextView mAccessLevel;
    @InjectView(R.id.createdValue) TextView mCreateDate;
    @InjectView(R.id.createdByValue) TextView mCreatedBy;
    @InjectView(R.id.lastUpdatedValue) TextView mLastUpdatedValue;
    @InjectView(R.id.userUpdateValue) TextView mUserUpdated;
    @InjectView(R.id.shapeTypeValue) TextView mShapeType;
    @InjectView(R.id.entityCountValue) TextView mFeatureCount;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_details_tab, container, false);

        ButterKnife.inject(this, v);

        mLayerDialog = application.getUIHelperComponent().provideLayerDialog();

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().LayerDetailScreen());

        refresh();

        return v;
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideLayerDetailsTask();
        mTask.getDetails(new GetLayerDetailParams(mEntity, mDetails, this));
    }

    @Override
    public void onPostExecute(LayerDetailsVm details) {
        if(details != null){
            mFeatureCount.setText(details.getFeatureCount() + "");
            mCreateDate.setText(DateTimeFormatter.format(details.getCreateDateTime()));
            mCreatedBy.setText(details.getCreateUser());
            if (details.getUpdateUser().length() > 0) {
                mUserUpdated.setText(details.getUpdateUser());
                mLastUpdatedValue.setText(DateTimeFormatter.format(details.getUpdateDateTime()));
            } else {
                mUserUpdated.setVisibility(View.GONE);
                mLastUpdatedValue.setVisibility(View.GONE);
            }
            mShapeType.setText(mEntity.getReadableGeometryType());
        }
    }
}
