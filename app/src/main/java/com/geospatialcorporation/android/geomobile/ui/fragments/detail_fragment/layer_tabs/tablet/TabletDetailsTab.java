package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerDetailParams;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailsTabCommons.ILayerDetailsTabCommon;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailsTabCommons.LayerDetailsTabCommon;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import butterknife.Bind;

public class TabletDetailsTab extends TabGeoViewFragmentBase implements IPostExecuter<LayerDetailsVm> {

    Layer mEntity;
    LayerDetailsVm mDetails;

    ILayerDetailsTabCommon mCommons;

    @Bind(R.id.access_level) TextView mAccessLevel;
    @Bind(R.id.createdValue) TextView mCreateDate;
    @Bind(R.id.createdByValue) TextView mCreatedBy;
    @Bind(R.id.lastUpdatedValue) TextView mLastUpdatedValue;
    @Bind(R.id.userUpdateValue) TextView mUserUpdated;
    @Bind(R.id.shapeTypeValue) TextView mShapeType;
    @Bind(R.id.entityCountValue) TextView mFeatureCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCommons = new LayerDetailsTabCommon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mEntity = mCommons.handleArguments(getArguments());

        //mCommons.setViews(...)

        mCommons.getDetailsAsync(new GetLayerDetailParams(mEntity, mDetails, this));

        return v;
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

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_layer_details_tab;
    }
}
