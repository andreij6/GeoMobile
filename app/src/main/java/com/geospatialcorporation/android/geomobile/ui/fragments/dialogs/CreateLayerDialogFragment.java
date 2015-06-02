package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.LayerTreeService;

/**
 * Created by andre on 6/1/2015.
 */
public class CreateLayerDialogFragment extends CreateDialogFragmentBase {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        final LayerTreeService layerService = new LayerTreeService();

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

                Boolean success = layerService.createLayer(name.getText().toString(), shape, mFolder.getId());

                if(success){
                    Toast.makeText(mContext, "Layer Created", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Layer was not Created", Toast.LENGTH_LONG).show();
                }
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
