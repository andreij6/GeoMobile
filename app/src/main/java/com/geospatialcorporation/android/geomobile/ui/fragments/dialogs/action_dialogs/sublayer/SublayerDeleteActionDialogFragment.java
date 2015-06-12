package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;

/**
 * Created by andre on 6/12/2015.
 */
public class SublayerDeleteActionDialogFragment extends SublayerActionDialogFragmentBase {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mSublayer.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mService.delete(mSublayer.getId());

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
