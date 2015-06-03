package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.FolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

/**
 * Created by andre on 6/2/2015.
 */
public class DeleteFolderDialogFragment extends GeoDialogFragmentBase {
    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }

    Folder mFolder;
    FolderTreeService mService;

    public void init(Context context, Folder folder){
        setContext(context);
        setFolder(folder);
        mService = new FolderTreeService();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mFolder.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mService.deleteFolder(mFolder);

                        Toast.makeText(application.getAppContext(), "Delete Request Sent!", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
