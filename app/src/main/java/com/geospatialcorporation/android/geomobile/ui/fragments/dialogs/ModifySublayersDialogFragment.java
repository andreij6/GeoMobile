package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.BookmarkSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/8/2015.
 */
public class ModifySublayersDialogFragment extends GeoDialogFragmentBase {

    public void init(Context context){
       setContext(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_modify_sublayers);

        Button show = (Button)v.findViewById(R.id.button55);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifySublayersDialogFragment.this.getDialog().cancel();
            }
        });

        builder.setTitle(R.string.bookmark)
                .setView(v)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
