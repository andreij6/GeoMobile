package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;


public class CreateLayerDialogFragment extends CreateDialogFragmentBase {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        final ILayerTreeService layerService = application.getTreeServiceComponent().provideLayerTreeService();

        View v = getDialogView(R.layout.dialog_create_layer);

        final EditText name = (EditText)v.findViewById(R.id.layerNameInput);
        final RadioButton point = (RadioButton)v.findViewById(R.id.pointRadio);
        final RadioButton polygon = (RadioButton)v.findViewById(R.id.polygonRadio);
        final RadioButton line = (RadioButton)v.findViewById(R.id.lineRadio);

        builder.setTitle(R.string.create_layer);

        builder.setView(v);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int shape = 0;

                if(point.isChecked())
                    shape = GeometryTypeCodes.Point;

                if(polygon.isChecked())
                    shape = GeometryTypeCodes.Polygon;

                if(line.isChecked())
                    shape = GeometryTypeCodes.Line;

                mAnalytics.trackClick(new GoogleAnalyticEvent().CreateLayer());

                layerService.create(name.getText().toString(), shape, mFolder.getId());

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateLayerDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

}
