package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayerDetailPanelFragment extends GeoViewFragmentBase {

    LayerDetailFragment mContentFragment;
    Layer mLayer;
    IAttributeDialog mAttributeDialog;
    ILayerDialog mLayerDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_panel_layerdetail, container, false);
        ButterKnife.bind(this, view);

        mContentFragment = (LayerDetailFragment)application.getMainActivity().getContentFragment();
        mLayerDialog = application.getUIHelperComponent().provideLayerDialog();
        mAttributeDialog = application.getUIHelperComponent().provideAttributeDialog();

        handleArgs();

        return view;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mLayer = args.getParcelable(Layer.LAYER_INTENT);
    }


    @OnClick(R.id.close)
    public void close(){
        mContentFragment.closePanel();
    }

    @OnClick(R.id.addAttributeSection)
    public void addAttribute(){
        mAttributeDialog.create(mLayer, getActivity(), getFragmentManager());

        mContentFragment.closePanel();
    }

    @OnClick(R.id.renameSection)
    public void rename(){
        mLayerDialog.rename(mLayer, getActivity(), getFragmentManager());

        mContentFragment.closePanel();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mLayerDialog.delete(mLayer, getActivity(), getFragmentManager());

        mContentFragment.closePanel();
    }

}
