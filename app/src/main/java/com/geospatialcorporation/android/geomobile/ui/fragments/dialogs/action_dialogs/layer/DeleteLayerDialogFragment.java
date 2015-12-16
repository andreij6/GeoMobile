package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;


public class DeleteLayerDialogFragment extends LayerActionDialogBase {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(savedInstanceState != null){
            mLayer = savedInstanceState.getParcelable(LAYER_DIALOG);
            init(mLayer);
        }

        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mLayer.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mService.delete(mLayer.getId());
                        Toaster("Deleting " + mLayer.getName());
                        goToMapFragment();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }

    private void goToMapFragment() {
        Fragment googleMapFragment = new GoogleMapFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, googleMapFragment)
                .addToBackStack(null)
                .commit();
    }
}
