package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/10/2015.
 */
public class FolderActionsDialogFragment extends GeoDialogFragmentBase {

    Folder mFolder;

    public void init(Context context, Folder folder){
        setContext(context);
        mFolder = folder;
    }

    @Override
    public void onStart(){
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        if(d != null){
            Button negative = d.getButton(Dialog.BUTTON_NEGATIVE);
            negative.setEnabled(false);
        }
    }

    //region ButterKnife
    @InjectView(R.id.renameSection) LinearLayout mRenameSection;
    @InjectView(R.id.deleteSection) LinearLayout mDeleteSection;
    @InjectView(R.id.renameImageView) ImageView mRenameIV;
    @InjectView(R.id.deleteImageView) ImageView mDeleteIV;
    @InjectView(R.id.deleteTV) TextView mDeleteTV;
    @InjectView(R.id.renameTV) TextView mRenameTV;
    //endregion

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_folder_actions);

        ButterKnife.inject(this, v);

        mDeleteIV.setOnClickListener(deleteFolder);
        mDeleteTV.setOnClickListener(deleteFolder);

        mRenameIV.setOnClickListener(renameFolder);
        mRenameTV.setOnClickListener(renameFolder);

        builder.setTitle(R.string.folder_actions)
                .setView(v)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return builder.create();
    }

    protected View.OnClickListener renameFolder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GeoDialogHelper.renameFolder(getContext(), mFolder, getFragmentManager());
            FolderActionsDialogFragment.this.getDialog().cancel();
        }
    };

    protected View.OnClickListener deleteFolder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GeoDialogHelper.deleteFolder(getContext(), mFolder, getFragmentManager());
            FolderActionsDialogFragment.this.getDialog().cancel();
        }
    };
}