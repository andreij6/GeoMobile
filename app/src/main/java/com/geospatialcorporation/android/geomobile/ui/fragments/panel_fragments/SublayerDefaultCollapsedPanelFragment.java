package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.SublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/19/2015.
 */
public class SublayerDefaultCollapsedPanelFragment extends GeoViewFragmentBase {

    Layer mLayer;
    SublayerTreeService mService;

    @InjectView(R.id.create) Button mCreateBtn;
    @InjectView(R.id.nameET) EditText mNameET;
    @InjectView(R.id.toggle) Button mToggle;

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
        ButterKnife.inject(this, mView);
        setPanelManager(GeoPanel.SUBLAYER);
        mPanelManager.touch(false);

        handleArgs();

        mService = new SublayerTreeService();

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
