package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/10/2015.
 */
public class FolderActionsDialogFragment extends GeoDialogFragmentBase {

    public void init(Context context){
        setContext(context);
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

    @OnClick(R.id.renameSection)
    public void renameFolder(){
        Toaster("Show rename Folder mode");
    }

    @OnClick(R.id.deleteSection)
    public void deleteFolder(){
        Toaster("Show delete Folder mode");
    }
    //endregion

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_folder_actions);

        ButterKnife.inject(v);

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
}
