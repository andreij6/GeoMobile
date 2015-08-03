package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SublayerCreateActionDialogFragment extends SublayerActionDialogFragmentBase {

    @InjectView(R.id.renameInput) EditText mName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.inject(this, v);

        return getDialogBuilder()
                .setTitle(R.string.add_sublayer)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mName.getText().toString();

                        if (!newName.isEmpty() && newName.length() > 2) {
                            SublayerCreateRequest model = new SublayerCreateRequest();
                            model.setParentId(mEntity.getId());
                            model.setName(newName);

                            mService.createSublayer(model);
                        } else {
                            Toaster("Please add a valid Name");
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
