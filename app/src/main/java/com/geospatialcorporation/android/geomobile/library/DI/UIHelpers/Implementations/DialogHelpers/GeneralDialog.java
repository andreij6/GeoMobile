package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapFeatureDocumentDialogFragment;

public class GeneralDialog implements IGeneralDialog {

    ILayerTreeService mService;

    public GeneralDialog(){
        mService = application.getTreeServiceComponent().provideLayerTreeService();
    }

    @Override
    public void addMapFeatureDocument(int layerId, String featureId, Context context, FragmentManager manager) {
        MapFeatureDocumentDialogFragment mfddf = new MapFeatureDocumentDialogFragment();
        mfddf.init(context, layerId, featureId);
        mfddf.show(manager, "Add Document to Feature");
    }

    @Override
    public void editAttributes(Context context, FragmentManager manager) {

    }

    @Override
    public void removeMapFeatureDocument(final RemoveMapFeatureDocumentRequest request) {
        new MaterialDialog.Builder(request.getContext())
                .title(R.string.are_you_sure_title)
                .content(request.getMessage())
                .positiveText(R.string.i_am_sure)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mService.removeMapFeatureDocument(request);
                        dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.cancel();
                    }
                }).show();
    }


}
