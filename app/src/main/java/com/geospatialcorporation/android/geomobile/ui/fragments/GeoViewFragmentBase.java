package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;

import butterknife.ButterKnife;

/**
 * Created by andre on 6/4/2015.
 */
public class GeoViewFragmentBase extends Fragment {

    OnFragmentInteractionListener mListener;

    protected View mView;
    protected SlidingPanelManager mPanelManager;

    protected void setView(LayoutInflater inflater, ViewGroup container, int layout) {
        mView = inflater.inflate(layout, container, false);
        ButterKnife.inject(this, mView);
    }

    protected void setPanelManager() {
        mPanelManager = new SlidingPanelManager(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void SetTitle(int stringResource){
        if (mListener != null) {
            mListener.onFragmentInteraction(getActivity().getString(stringResource));
        }
    }

    protected void SetTitle(String string){
        if (mListener != null) {
            mListener.onFragmentInteraction(string);
        }
    }

    protected void Toaster(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
