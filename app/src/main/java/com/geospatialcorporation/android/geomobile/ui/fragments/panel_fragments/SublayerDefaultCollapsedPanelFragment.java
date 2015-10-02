package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SublayerDefaultCollapsedPanelFragment extends GeoViewFragmentBase {

    Layer mLayer;
    ISublayerTreeService mService;

    @Bind(R.id.create) Button mCreateBtn;
    @Bind(R.id.nameET) EditText mNameET;
    @Bind(R.id.toggle) Button mToggle;

    @OnClick(R.id.create)
    public void createSublayer(){
        if(Valid()){

            SublayerCreateRequest request = new SublayerCreateRequest();
            request.setParentId(mLayer.getId());
            request.setName(getValue(mNameET));

            mService.createSublayer(request);

            mPanelManager.toggle();
        }
    }

    @OnClick(R.id.toggle)
    public void toggle(){
        mPanelManager.toggle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_panel_sublayer_collapsed, container, false);
        ButterKnife.bind(this, mView);
        setPanelManager(GeoPanel.SUBLAYER);
        mPanelManager.touch(false);

        handleArgs();

        mService = application.getTreeServiceComponent().provideSublayerTreeService();

        return mView;
    }



    @Override
    public void onResume(){
        super.onResume();
        mPanelManager.collapse();
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mLayer = args.getParcelable(Layer.LAYER_INTENT);
    }

    protected Boolean Valid(){
        return !getValue(mNameET).equals("");
    }

}
