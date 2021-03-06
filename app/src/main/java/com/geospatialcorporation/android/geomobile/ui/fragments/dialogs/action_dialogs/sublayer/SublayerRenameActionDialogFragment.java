package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SublayerRenameActionDialogFragment extends SublayerActionDialogFragmentBase {
    @Bind(R.id.renameInput) EditText mRenameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.bind(this, v);

        mRenameInput.setText(mEntity.getName());

        return getDialogBuilder()
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mRenameInput.getText().toString();

                        if (!newName.isEmpty()) {
                            mService.rename(mEntity.getId(), newName);
                            //getFragmentManager().popBackStack();
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
