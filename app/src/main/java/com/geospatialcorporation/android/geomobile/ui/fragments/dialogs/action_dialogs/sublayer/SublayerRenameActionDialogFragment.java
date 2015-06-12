package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/12/2015.
 */
public class SublayerRenameActionDialogFragment extends SublayerActionDialogFragmentBase {
    @InjectView(R.id.renameInput)
    EditText mRenameInput;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.inject(this, v);

        mRenameInput.setText(mSublayer.getName());

        return getDialogBuilder()
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mRenameInput.getText().toString();

                        if (!newName.isEmpty()) {
                            mService.rename(mSublayer.getId(), newName);
                            Toaster("Sublayer Renamed");
                            Toaster("Pull down Refresh");
                            getFragmentManager().popBackStack();
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
