package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.BookmarkSectionBuilder;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.BookmarkMode;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;
import com.google.android.gms.maps.GoogleMap;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/5/2015.
 */
public class BookmarksDialogFragment extends GeoDialogFragmentBase{

    FloatingActionButton mSaveBtn;
    FloatingActionButton mCloseBtn;
    SlidingUpPanelLayout mPanel;
    GoogleMap mMap;
    FragmentManager mFragmentManager;

    public void init(Context context, FloatingActionButton s, FloatingActionButton c, SlidingUpPanelLayout p,  GoogleMap m, FragmentManager fm){
        setContext(context);
        mSaveBtn = s;
        mCloseBtn = c;
        mPanel = p;
        mMap = m;
        mFragmentManager = fm;
    }

    @Override
    public void onStart(){
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        if(d != null){
            Button negative = d.getButton(Dialog.BUTTON_NEGATIVE);
            negative.setEnabled(false);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_bookmarks);

        LinearLayout l = (LinearLayout)v.findViewById(R.id.addBookmarkContainer);
        l.setOnClickListener(addBookmarkMode);

        RecyclerView recycler = (RecyclerView)v.findViewById(R.id.bookmarkRecycler);

        new BookmarkSectionBuilder(mContext)
                .BuildAdapter(getData(), 0)
                .setRecycler(recycler);


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

    private View.OnClickListener addBookmarkMode = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Toaster("Set Bookmark mode");
            Fragment contentFragment = ((MainActivity)getActivity()).getContentFragment();

            ((IViewModeListener)contentFragment).setViewMode(
                    new BookmarkMode.Builder()
                            .init(mSaveBtn, mCloseBtn, new SlidingPanelManager(getActivity()), mMap, mFragmentManager)
                            .create()
            );
            BookmarksDialogFragment.this.getDialog().cancel();
        }
    };


    public List<Bookmark> getData() {
        return new ArrayList<Bookmark>();
    }
}
