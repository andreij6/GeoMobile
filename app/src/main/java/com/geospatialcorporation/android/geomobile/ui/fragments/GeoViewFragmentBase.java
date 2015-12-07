package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.AnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.DaggerAnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;

import butterknife.ButterKnife;

public abstract class GeoViewFragmentBase extends Fragment {

    OnFragmentInteractionListener mListener;

    protected View mView;
    protected ISlidingPanelManager mPanelManager;
    protected IGeoAnalytics mAnalytics;
    private AnalyticsComponent mAnalyticsComponent;
    protected Boolean mIsLandscape;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);
        return mView;
    }

    protected void setView(LayoutInflater inflater, ViewGroup container, int layout) {
        mView = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, mView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnalyticsComponent = DaggerAnalyticsComponent.builder().build();
        mAnalytics = mAnalyticsComponent.provideGeoAnalytics();
        mIsLandscape = application.getIsLandscape();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(!DeviceTypeUtil.isTablet(getResources())) {
            try {
                mListener = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException e) {

            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void SetTitle(int stringResource){
        //if (mListener != null) {
        //    mListener.onFragmentInteraction(getActivity().getString(stringResource));
        //}
    }

    public void SetTitle(String title){
        //if (mListener != null) {
        //    mListener.onFragmentInteraction(string);
        //}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void Toaster(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void setPanelManager(int panelId) {
        mPanelManager = new PanelManager(panelId);
    }

    public String getValue(EditText et){
        return et.getText().toString();
    }

    public String getValue(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }



}
