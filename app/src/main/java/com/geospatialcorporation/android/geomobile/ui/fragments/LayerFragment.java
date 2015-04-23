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
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerFragment extends Fragment {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private List<Folder> mDataSet;
    private View mRootView;
    private TreeService mService;
    List<Layer> mLayers;
    Folder mCurrentFolder;
    DataHelper mHelper;

    RecyclerView.LayoutManager mLM;

    public LayerFragment(){
        mHelper = new DataHelper();
    }

    @InjectView(R.id.layer_recyclerView) RecyclerView mRecycler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        ButterKnife.inject(this, mRootView);

        mService = application.getRestAdapter().create(TreeService.class);

        new GetLayersTask().execute();

        mLM = new LinearLayoutManager(container.getContext());

        return mRootView;
    }

    private class GetLayersTask extends AsyncTask<Void, Void, List<Folder>>{

        @Override
        protected List<Folder> doInBackground(Void... params) {
            try{
                List<Folder> folders = mService.getLayers();

                mCurrentFolder = folders.get(0);

                List<Folder> allFolders = mHelper.GetFoldersRecursively(mCurrentFolder);
                List<Layer> layers = mHelper.GetLayersRecursively(mCurrentFolder);

                application.setLayerFolders(allFolders);
                application.setLayers(layers);

                mDataSet = mCurrentFolder.getFolders(); //Get Subfolders

                mLayers = mCurrentFolder.getLayers();

            }catch(RetrofitError e){
                Log.d(TAG, "mESSed up");
            }

            return mDataSet;
        }

        @Override
        protected void onPostExecute(List<Folder> folders){
            List<ListItem> listItems = mHelper.CombineLayerItems(mLayers, mDataSet);

            ListItemAdapter listItemAdapter = new ListItemAdapter(getActivity(), listItems, ListItemAdapter.LAYER);

            List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Folders"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(mDataSet.size(), "Layers"));

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(getActivity(),  R.layout.section, R.id.section_text, listItemAdapter);

            mSectionedAdapter.setSections(sections.toArray(dummy));

            mRecycler.setAdapter(mSectionedAdapter);

            mRecycler.setLayoutManager(mLM);
        }
    }



}
