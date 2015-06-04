package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

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
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayerActionDialogFragment extends GeoDialogFragmentBase {
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
    @InjectView(R.id.addLayerFolderSection) LinearLayout mFolderSection;
    @InjectView(R.id.addLayerSection) LinearLayout mLayerSection;
    //endregion

    //region OnClick
    @OnClick(R.id.addLayerSection)
    public void layerSectionClicked(){
        highlight(mLayerSection);
        unhighlight(mFolderSection);
    }

    @OnClick(R.id.addLayerFolderSection)
    public void folderSectionClicked(){
        highlight(mFolderSection);
        unhighlight(mLayerSection);
    }

    private void highlight(LinearLayout layout){
        layout.setBackgroundColor(Color.LTGRAY);
    }

    private void unhighlight(LinearLayout layout){
        layout.setBackgroundColor(Color.WHITE);
    }
    //endregion

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        final View v = inflater.inflate(R.layout.dialog_layer, null);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.layer_dialog_title);
        builder.setView(v);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LinearLayout f = (LinearLayout)v.findViewById(R.id.addLayerFolderSection);
                LinearLayout l = (LinearLayout)v.findViewById(R.id.addLayerSection);

                if (isHighlighted(f)) {
                    GeoDialogHelper.createFolder(mContext, mFolder, getFragmentManager());
                }

                if(isHighlighted(l)){
                    GeoDialogHelper.createLayer(mContext, mFolder, getFragmentManager());
                }

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayerActionDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    private boolean isHighlighted(LinearLayout d) {
        Integer b = ((ColorDrawable)d.getBackground()).getColor();

        return !(b == Color.WHITE);
    }

}
