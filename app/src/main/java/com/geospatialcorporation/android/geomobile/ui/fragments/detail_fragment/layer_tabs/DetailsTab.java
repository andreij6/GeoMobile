package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.services.SublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.adapters.SublayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.List;
import java.util.TooManyListenersException;

import butterknife.ButterKnife;

/**
 * Created by andre on 6/7/2015.
 */
public class DetailsTab extends GeoDetailsTabBase<Layer> {

    LayerDetailsVm mDetails;

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
                Toast.makeText(getActivity(), details.getFeatureCount() + "", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), details.getCreateUser(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), details.getCreateDateTime(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), details.getUpdateDateTime(), Toast.LENGTH_LONG).show();
            }else{
                Toaster("details null");
            }
        }


    }

}
