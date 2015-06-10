package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/8/2015.
 */
public class DocumentFolderContentsTab extends GeoDetailsTabBase<Folder> {
    private static final String TAG = DocumentFolderContentsTab.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_document_folder_contents_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Folder.FOLDER_INTENT);
        handleArgs();

        return v;
    }

}
