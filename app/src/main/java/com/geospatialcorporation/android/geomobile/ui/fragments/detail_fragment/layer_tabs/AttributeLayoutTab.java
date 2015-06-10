package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by andre on 6/7/2015.
 */
public class AttributeLayoutTab extends GeoDetailsTabBase<Layer> {

    private static final String TAG = AttributeLayoutTab.class.getSimpleName();

    List<LayerAttributeColumns> mAttributeColumns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_attributes_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        new GetAttributeColumns().execute();

        return v;
    }

    private class GetAttributeColumns extends AsyncTask<Void, Void, List<LayerAttributeColumns>> {

        @Override
        protected List<LayerAttributeColumns> doInBackground(Void... params) {
            mService = new LayerTreeService();

            if(mEntity != null){
                Log.d(TAG, "LAYER not Null");
                mAttributeColumns = ((LayerTreeService)mService).getLayerAttributeColumns(mEntity.getId());
            }

            Log.d(TAG, "Returning Attr Columns");
            return mAttributeColumns;
        }

        @Override
        protected void onPostExecute(List<LayerAttributeColumns> attributes){
            Log.d(TAG, "About to loop");

            for(LayerAttributeColumns column : attributes){
                Toast.makeText(getActivity(), column.getName() + " : " + column.getDataTypeName(), Toast.LENGTH_LONG).show();
            }
        }


    }

}
