package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabLayerFolderDetailFragment;

import butterknife.OnClick;

public class TabletLayerFolderPanelFragment extends TabletTreeFolderPanelFragmentBase {
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_layerfolder;
    }

    //@OnClick(R.id.folderInfoSection)
    //public  void folderSection(){
    //    Fragment f = new TabLayerFolderDetailFragment();
    //
    //    f.setArguments(mFolder.toBundle());
    //
    //    getFragmentManager()
    //            .beginTransaction()
    //            .addToBackStack(null)
    //            .replace(R.id.info_frame, f)
    //            .commit();
    //}

    @OnClick(R.id.addLayerSection)
    public void layerSectionClicked(){
        ILayerDialog layerDialog = application.getUIHelperComponent().provideLayerDialog();

        layerDialog.create(mFolder, getActivity(), getFragmentManager());

        mPanelCtrl.closePanel();
    }

    @OnClick(R.id.addLayerFolderSection)
    public void folderSectionClicked(){
        IFolderDialog folderDialog = application.getUIHelperComponent().provideFolderDialog();

        folderDialog.create(mFolder, getActivity(), getFragmentManager());

        mPanelCtrl.closePanel();
    }

    @OnClick(R.id.close)
    public void close(){
        mPanelCtrl.closePanel();
    }
}
