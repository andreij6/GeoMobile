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
import com.geospatialcorporation.android.geomobile.models.LibraryItems.LibraryItem;
import com.geospatialcorporation.android.geomobile.ui.adapters.LibraryAdapter;

import java.io.IOException;
import java.util.ArrayList;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryFragment extends Fragment{

    //region Properties
    private RecyclerView mRecyclerView;
    private ArrayList<LibraryItem> mDataSet;
    private View mRootView;
    //endregion

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.libraryitem_recyclerView);

        //TODO: API CALL to set mDataSet
        RestService restService = new RestService.Builder()
                .endPoint(EndPoints.TREE_LIBRARY)
                .setAuthToken("WebToken esSCBBfw1zGRUq3XpEmsqLrlCzLytO6YRnP2v2p8g9u8VBf14vgS1C3gDgdv59RKZJ5wQo5tMoCk6pyYg/EXV5O4nUEUW8/lUR9fxc7R4TQKtWPMs4lh4S6Q5zW6lOWK")
                .callBack(treeLibraryCallback)
                .build();

        restService.Send();

        mDataSet = new ArrayList<>();

        LibraryAdapter adapter = new LibraryAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    //region treeLibraryCallback
    protected CallbackAction treeLibrarySuccess = new CallbackAction(){
        @Override
        public void run(Response response){
            try {
                String responseString = response.body().string();

                JSONArray message = new JSONArray(responseString);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "success - library", Toast.LENGTH_LONG).show();

                    }
                });
            }
            catch (IOException e){

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    protected Callback treeLibraryCallback = CallbackHelper.runInsideShell(treeLibrarySuccess, CallbackHelper.StandardFailure(getActivity()));
    //endregion
}
