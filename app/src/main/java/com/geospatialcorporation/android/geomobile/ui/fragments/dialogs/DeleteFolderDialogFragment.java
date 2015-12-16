package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder.FolderActionDialogBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;


public class DeleteFolderDialogFragment extends FolderActionDialogBase {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        if(savedInstanceState != null){
            mFolder = savedInstanceState.getParcelable(FOLDER_DIALOG);
            init(mFolder);
        }

        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mFolder.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(application.getAppContext(), mContext.getString(R.string.delete_request_sent), Toast.LENGTH_LONG).show();

                        mService.delete(mFolder.getId());
                        mAnalytics.trackClick(new GoogleAnalyticEvent().DeleteFolder());
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
