package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.database.datasource.Layers.LayersDataSource;
import com.geospatialcorporation.android.geomobile.library.constants.EndPoints;
import com.geospatialcorporation.android.geomobile.library.rest.CallbackAction;
import com.geospatialcorporation.android.geomobile.library.rest.CallbackHelper;
import com.geospatialcorporation.android.geomobile.library.rest.RestService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.LayerAdapter;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
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

        RestService restService = new RestService.Builder()
                                                    .endPoint(EndPoints.TREE_LAYER)
                                                    .setAuthToken(mTempAuthValue)
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
        public void run(Response response) {
            try {
                String responseString = response.body().string();

                JSONArray message = new JSONArray(responseString);


                String rootFolderString = message.get(0).toString();

                Gson gson = new Gson();

                Folder folder = gson.fromJson(rootFolderString, Folder.class);

                mDataSet = GetLayersRecursively(folder);

                LayersDataSource dataSource = new LayersDataSource(getActivity());

                dataSource.Create(mDataSet);

                //mDataSet = (ArrayList<Layer>)dataSource.GetAll();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        LayerAdapter adapter = new LayerAdapter(getActivity(), mDataSet);

                        mRecyclerView.setAdapter(adapter);

                    }
                });
            } catch (IOException e){

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    private ArrayList<Layer> GetLayersRecursively(Folder folder) {
        ArrayList<Layer> result = new ArrayList<>();

        if(folder.getFolders().size() == 0){
            result.addAll(folder.getLayers());
        } else {

            for(Folder x : folder.getFolders()){
                result.addAll(GetLayersRecursively(x));
            }
        }
        return result;
    }

    protected Callback treeLayerCallback = CallbackHelper.runInsideShell(treeLayerSuccess, CallbackHelper.StandardFailure(getActivity()));
    //endregion


}
