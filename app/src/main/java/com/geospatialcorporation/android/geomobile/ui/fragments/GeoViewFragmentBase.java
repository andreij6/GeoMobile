package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.AnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.DaggerAnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMainMenuHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class GeoViewFragmentBase extends Fragment {

    OnFragmentInteractionListener mListener;

    protected View mView;
    protected ISlidingPanelManager mPanelManager;
    protected IGeoAnalytics mAnalytics;
    private AnalyticsComponent mAnalyticsComponent;
    protected IMainMenuHelper mNavigationHelper;


    protected void setView(LayoutInflater inflater, ViewGroup container, int layout) {
        mView = inflater.inflate(layout, container, false);
        ButterKnife.inject(this, mView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnalyticsComponent = DaggerAnalyticsComponent.builder().build();
        mAnalytics = mAnalyticsComponent.provideGeoAnalytics();
        mNavigationHelper = application.getUIHelperComponent().provideMainMenuHelper();
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
