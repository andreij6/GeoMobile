package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RenameLayerActionDialogFragment extends LayerActionDialogBase {
    @Bind(R.id.renameInput)
    EditText mRenameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(savedInstanceState != null){
            mLayer = savedInstanceState.getParcelable(LAYER_DIALOG);
            init(mLayer);
        }

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.bind(this, v);

        mRenameInput.setText(mLayer.getName());
        showKeyboard(mRenameInput);

        return getDialogBuilder()
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mRenameInput.getText().toString();

                        if (!newName.isEmpty()) {
                            mService.rename(mLayer.getId(), newName);
                            getFragmentManager().popBackStack();
                            hideKeyBoard(mRenameInput);
                        } else {
                            Toaster("Please add a valid Name");
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        hideKeyBoard(mRenameInput);
                    }
                }).create();
    }
}
