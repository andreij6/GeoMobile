package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapFeatureFiles;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.FeatureWindow.DocumentVMAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureDocumentsTab extends FeatureTabBase {

    @SuppressWarnings("unused")
    @OnClick(R.id.addDocument)
    public void addDocument(){
        Toaster("Not Implmented");
        //GeoDialogHelper.addMapFeatureDocument();
    }

    @InjectView(R.id.featureWindowDocumentsRecycler) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_documents_tab;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setRecycler() {
        List<FeatureWindowDocumentVM> dvms = getDocumentVMList(mResponse.getFeatures().get(0).getFiles());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(manager);

        DocumentVMAdapter adapter = new DocumentVMAdapter(getActivity(), dvms);

        mRecyclerView.setAdapter(adapter);
    }

    protected List<FeatureWindowDocumentVM> getDocumentVMList(List<MapFeatureFiles> files) {
        List<FeatureWindowDocumentVM> result = new ArrayList<>();

        if(files != null) {
            for (MapFeatureFiles file : files) {
                result.add(new FeatureWindowDocumentVM(file));
            }
        }

        return result;
    }

    public class FeatureWindowDocumentVM {
        String mFileName;
        String mFileSize;

        public FeatureWindowDocumentVM(MapFeatureFiles file) {
            mFileName = file.nameWithExt();
            mFileSize = file.getSize() + "";
        }

        //region Gs & Ss
        public String getFileSize() {
            return mFileSize;
        }

        public void setFileSize(String fileSize) {
            mFileSize = fileSize;
        }

        public String getFileName() {
            return mFileName;
        }

        public void setFileName(String fileName) {
            mFileName = fileName;
        }
        //endregion
    }
}
