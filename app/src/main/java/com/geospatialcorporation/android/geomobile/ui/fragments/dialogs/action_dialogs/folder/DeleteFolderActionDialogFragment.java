package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.R;

/**
 * Created by andre on 6/12/2015.
 */
public class DeleteFolderActionDialogFragment extends FolderActionDialogBase {
    private static final String TAG = DeleteFolderActionDialogFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mFolder.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mService.delete(mFolder);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
