package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.UIHelperComponent;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayerTreeActionDialogFragment extends GeoDialogFragmentBase {
    //region Getters & Setters
    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion

    //region Properties
    Folder mFolder;
    ILayerDialog mLayerDialog;
    IFolderDialog mFolderDialog;
    @InjectView(R.id.addLayerFolderSection) LinearLayout mFolderSection;
    @InjectView(R.id.addLayerSection) LinearLayout mLayerSection;
    //endregion

    //region OnClick
    @OnClick(R.id.addLayerSection)
    public void layerSectionClicked(){
        mLayerDialog.create(mFolder, mContext, getFragmentManager());

        LayerTreeActionDialogFragment.this.getDialog().cancel();
    }

    @OnClick(R.id.addLayerFolderSection)
    public void folderSectionClicked(){
        mFolderDialog.create(mFolder, mContext, getFragmentManager());

        LayerTreeActionDialogFragment.this.getDialog().cancel();
    }

    //endregion

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        UIHelperComponent component = application.getUIHelperComponent();

        mFolderDialog = component.provideFolderDialog();
        mLayerDialog = component.provideLayerDialog();

        final View v = inflater.inflate(R.layout.dialog_layer, null);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.layer_dialog_title);
        builder.setView(v);

        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

}
