package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;

import butterknife.OnClick;

public class LayerFolderPanelFragment extends TreeFolderPanelFragmentBase {

    @Override
    protected int getViewResource() {
        return R.layout.fragment_panel_layerfolder;
    }

    //On Clicks
    @OnClick(R.id.folderInfoSection)
    public  void folderSection(){
        Fragment f = new LayerFolderDetailFragment();

        f.setArguments(mFolder.toBundle());

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, f)
                .commit();
    }

    @OnClick(R.id.addLayerSection)
    public void layerSectionClicked(){
        ILayerDialog layerDialog = application.getUIHelperComponent().provideLayerDialog();

        layerDialog.create(mFolder, getActivity(), getFragmentManager());
    }

    @OnClick(R.id.addLayerFolderSection)
    public void folderSectionClicked(){
        IFolderDialog folderDialog = application.getUIHelperComponent().provideFolderDialog();

        folderDialog.create(mFolder, getActivity(), getFragmentManager());
    }
}
