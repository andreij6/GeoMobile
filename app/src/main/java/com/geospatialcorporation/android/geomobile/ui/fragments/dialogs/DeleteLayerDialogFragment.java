package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/2/2015.
 */
public class DeleteLayerDialogFragment extends GeoDialogFragmentBase {

    public Layer getLayer() {
        return mLayer;
    }

    public void setLayer(Layer layer) {
        mLayer = layer;
    }

    Layer mLayer;
    LayerTreeService Service;

    public void init(Context context, Layer layer){
        setContext(context);
        setLayer(layer);
        Service = new LayerTreeService();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mLayer.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Service.deleteLayer(mLayer.getId());

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
