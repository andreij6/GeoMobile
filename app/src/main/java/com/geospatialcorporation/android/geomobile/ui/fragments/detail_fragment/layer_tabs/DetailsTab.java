package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/7/2015.
 */
public class DetailsTab extends GeoDetailsTabBase<Layer> {

    LayerDetailsVm mDetails;

    @InjectView(R.id.access_level) TextView mAccessLevel;
    @InjectView(R.id.createdValue) TextView mCreateDate;
    @InjectView(R.id.createdByValue) TextView mCreatedBy;
    @InjectView(R.id.lastUpdatedValue) TextView mLastUpdatedValue;
    @InjectView(R.id.userUpdateValue) TextView mUserUpdated;
    @InjectView(R.id.shapeTypeValue) TextView mShapeType;
    @InjectView(R.id.entityCountValue) TextView mFeatureCount;
    @InjectView(R.id.fab) FloatingActionButton mEdit;

    @OnClick(R.id.fab)
    public void showLayerActions(){
        GeoDialogHelper.showLayerActions(getActivity(), mEntity, getActivity().getSupportFragmentManager());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_details_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        new GetDetailsTask().execute();

        return v;
    }

    private class GetDetailsTask extends AsyncTask<Void, Void, LayerDetailsVm> {

        @Override
        protected LayerDetailsVm doInBackground(Void... params) {
            mService = new LayerTreeService();

            if(mEntity != null){
                mDetails = ((LayerTreeService)mService).getDetails(mEntity.getId());
            }

            return mDetails;
        }

        @Override
        protected void onPostExecute(LayerDetailsVm details){
            if(details != null) {
                mFeatureCount.setText(details.getFeatureCount() + "");
                mCreateDate.setText(details.getCreateDateTime());
                mCreatedBy.setText(details.getCreateUser());
                mUserUpdated.setText(details.getUpdateUser());
                mLastUpdatedValue.setText(details.getUpdateDateTime());
                mShapeType.setText(mEntity.getReadableGeometryType());
            }else{
                Toaster("details null");
            }
        }


    }

}
