package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.NodeTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResult;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoRecyclerAdapterBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class QuickSearchAdapter extends GeoRecyclerAdapterBase<QuickSearchAdapter.Holder, QuickSearchResponse> {

    public QuickSearchAdapter(Context context, List<QuickSearchResponse> data){
        super(context, data, R.layout.recycler_quick_search, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<QuickSearchResponse> {

        @InjectView(R.id.nodeTypeIV) ImageView mNodeTypeIV;
        @InjectView(R.id.nodeTypeName) TextView mNodeTypeTV;
        @InjectView(R.id.resultListView) ListView mResults;

        public Holder(View v){ super(v);}

        @Override
        public void bind(QuickSearchResponse response) {
            switch (response.getType()){
                case NodeTypeCodes.FOLDER:
                    showFolderResults(response.getResults());
                    break;
                case NodeTypeCodes.LAYER:
                    showLayerResults(response.getResults());
                    break;
                case NodeTypeCodes.SUBLAYER:
                    showSublayerResults(response.getResults());
                    break;
                case NodeTypeCodes.DOCUMENT:
                    showDocumentResults(response.getResults());
                    break;
                case NodeTypeCodes.MAPFEATURE:
                    showMapFeatureResults(response.getResults());
                    break;
                default:
                    break;
            }
        }

        //region ShowResults
        protected void showFolderResults(List<QuickSearchResult> results) {
            setNamedResults(results);
            setNodeName("Results Found in Folders");
            setDrawable(R.drawable.ic_folder_black_18dp);
        }

        protected void showLayerResults(List<QuickSearchResult> results) {
            setNamedResults(results);
            setNodeName("Results Found in Layers");
            setDrawable(R.drawable.ic_layers_black_18dp);
        }

        protected void showSublayerResults(List<QuickSearchResult> results) {
            setNamedResults(results);
            setNodeName("Results Found in Sublayers");
            setDrawable(R.drawable.ic_layers_black_18dp);
        }

        protected void showDocumentResults(List<QuickSearchResult> results) {
            setDocumentResults(results);
            setNodeName("Results Found in Documents");
            setDrawable(R.drawable.ic_file_document_box_black_18dp);
        }

        protected void showMapFeatureResults(List<QuickSearchResult> results) {
            setMapFeatureResults(results);
            setNodeName("Results Found in MapFeatures");
            setDrawable(R.drawable.ic_layers_black_18dp);
        }

        protected void setDrawable(int id){
            mNodeTypeIV.setImageDrawable(mContext.getDrawable(id));
        }

        protected void setNamedResults(List<QuickSearchResult> results){
            List<String> values = new ArrayList<>();

            for(QuickSearchResult result : results){
                if(result.getName() != null) {
                    values.add(result.getName());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mResults.setAdapter(adapter);
        }

        protected void setDocumentResults(List<QuickSearchResult> results){
            List<String> values = new ArrayList<>();

            for(QuickSearchResult result : results){
                if(result.getName() != null) {
                    values.add(result.getName() + result.getExt());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mResults.setAdapter(adapter);
        }

        protected void setMapFeatureResults(List<QuickSearchResult> results){
            List<String> values = new ArrayList<>();

            for(QuickSearchResult result : results){
                if(result.getLayerName() != null) {
                    values.add(result.getLayerName() + " : " + result.getValue());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mResults.setAdapter(adapter);
        }

        protected void setNodeName(String name){
            mNodeTypeTV.setText(name);
        }
        //endregion
    }

}
