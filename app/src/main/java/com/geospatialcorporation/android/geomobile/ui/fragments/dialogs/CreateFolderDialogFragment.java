package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;


public class CreateFolderDialogFragment extends CreateDialogFragmentBase {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();
        View v = getDialogView(R.layout.dialog_create_folder);
        final IFolderTreeService service = application.getTreeServiceComponent().provideFolderTreeService();

        final EditText name = (EditText)v.findViewById(R.id.folderNameInput);
        showKeyboard(name);

        builder.setTitle(R.string.create_folder)
                .setView(v)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Validation
                            mAnalytics.trackClick(new GoogleAnalyticEvent().CreateFolder());
                            service.create(name.getText().toString(), mFolder.getId());
                            hideKeyBoard(name);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateFolderDialogFragment.this.getDialog().cancel();
                            hideKeyBoard(name);
                        }
                    });

        return builder.create();
    }


}
