package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import butterknife.OnClick;

public class TabletLayerDetailPanelFragment extends TabletPanelFragmentBase {

    Layer mEntity;
    IAttributeDialog mAttributeDialog;
    ILayerDialog mLayerDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_layerdetail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        handleArgs();

        mLayerDialog = application.getUIHelperComponent().provideLayerDialog();
        mAttributeDialog = application.getUIHelperComponent().provideAttributeDialog();

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Layer.LAYER_INTENT);
    }

    @OnClick(R.id.close)
    public void close(){
        mPanelCtrl.closePanel();
    }

    @OnClick(R.id.addAttributeSection)
    public void addAttribute(){
        mAttributeDialog.create(mEntity, getActivity(), getFragmentManager());

        close();
    }

    @OnClick(R.id.renameSection)
    public void rename(){
        mLayerDialog.rename(mEntity, getActivity(), getFragmentManager());

        close();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mLayerDialog.delete(mEntity, getActivity(), getFragmentManager());

        close();
    }

}
