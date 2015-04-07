package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.EndPoints;
import com.geospatialcorporation.android.geomobile.library.util.rest.CallbackAction;
import com.geospatialcorporation.android.geomobile.library.util.rest.CallbackHelper;
import com.geospatialcorporation.android.geomobile.library.util.rest.RestService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.LayerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Layer> mDataSet;
    private View mRootView;

    private String mTempAuthValue = "WebToken esSCBBfw1zGRUq3XpEmsqLrlCzLytO6YRnP2v2p8g9u8VBf14vgS1C3gDgdv59RKZJ5wQo5tMoCk6pyYg/EXV5O4nUEUW8/lUR9fxc7R4TQKtWPMs4lh4S6Q5zW6lOWK";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.layeritem_recyclerView);

        //TODO: API CALL to set mDataSet
        RestService restService = new RestService.Builder()
                                                    .endPoint(EndPoints.TREE_LAYER)
                                                    .setAuthToken("WebToken esSCBBfw1zGRUq3XpEmsqLrlCzLytO6YRnP2v2p8g9u8VBf14vgS1C3gDgdv59RKZJ5wQo5tMoCk6pyYg/EXV5O4nUEUW8/lUR9fxc7R4TQKtWPMs4lh4S6Q5zW6lOWK")
                                                    .callBack(treeLayerCallback)
                                                    .build();
        restService.Send();

        mDataSet = new ArrayList<>();

        LayerAdapter adapter = new LayerAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    //region treeLayerCallback
    protected CallbackAction treeLayerSuccess = new CallbackAction(){
        @Override
        public void run(Response response){
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "success - layer", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    protected Callback treeLayerCallback = CallbackHelper.runInsideShell(treeLayerSuccess, CallbackHelper.StandardFailure(getActivity()));
    //endregion


}
