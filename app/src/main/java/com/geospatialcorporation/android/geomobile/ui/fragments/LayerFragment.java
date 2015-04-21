package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.LayerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerFragment extends Fragment {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private List<Layer> mDataSet;
    private View mRootView;
    private TreeService mService;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.layeritem_recyclerView);

        mService = application.getRestAdapter().create(TreeService.class);

        new GetLayersTask().execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    private class GetLayersTask extends AsyncTask<Void, Void, List<Layer>>{

        @Override
        protected List<Layer> doInBackground(Void... params) {
            try{
                List<Folder> folders = mService.getLayers();
                Folder rootFolder = folders.get(0);
                mDataSet = GetLayersRecursively(rootFolder);
            }catch(RetrofitError e){
                Log.d(TAG, "mESSed up");
            }

            return mDataSet;
        }

        @Override
        protected void onPostExecute(List<Layer> layers){

            LayerAdapter adapter = new LayerAdapter(getActivity(), mDataSet);

            mRecyclerView.setAdapter(adapter);
        }
    }

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

}
