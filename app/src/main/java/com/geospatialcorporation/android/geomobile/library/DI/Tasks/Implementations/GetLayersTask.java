package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersByFolderTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

public class GetLayersTask implements IGetLayersTask {
    private static final String TAG = GetLayersTask.class.getSimpleName();

    TreeService mTreeService;
    IFolderTreeService mFolderTreeService;
    LayerService mLayerService;
    DataHelper mDataHelper;
    IAddDataRepository<Folder> mFolderRepo;
    IAddDataRepository<Layer> mLayerRepo;

    public GetLayersTask(){
        mTreeService = application.getRestAdapter().create(TreeService.class);
        mDataHelper = new DataHelper();
        mFolderRepo = new FolderAppSource();
        mLayerRepo = new LayersAppSource();
        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();
        mLayerService = application.getRestAdapter().create(LayerService.class);
    }

    @Override
    public void getAll(GetLayersTaskParams params) {
        new GetAllLayersTask(params).execute(0);
    }

    @Override
    public void getByFolder(GetLayersByFolderTaskParams params, int folderId) {
        new GetByLayersFolderTask(params).execute(folderId);
    }

    protected void setPaths(List<Folder> folders) {
        for (Folder folder : folders ) {
            if(folder.getParent() != null){
                folder.setPath(getParentPath(folder.getParent()));
            }
        }
    }

    protected List<String> getParentPath(Folder parent) {
        List<String> paths = new ArrayList<>();

        if(parent.getParent() != null){
            paths.add(parent.getName());
            paths.addAll(getParentPath(parent.getParent()));
        } else {
            paths.add("ROOT");
        }

        return paths;
    }

    protected class GetAllLayersTask extends GeoAsyncTask<Integer, Void, List<Folder>> {

        DrawerLayout mDrawerLayout;

        public GetAllLayersTask(GetLayersTaskParams params){
            super(params.getExecuter());
            mDrawerLayout = params.getDrawer();
        }

        @Override
        protected List<Folder> doInBackground(Integer... params) {
            List<Folder> folders = new ArrayList<>();

            try {

                List<Folder> root = mTreeService.getLayers();

                folders = mDataHelper.getFoldersRecursively(root.get(0), null);

                setPaths(folders);

                int index = folders.indexOf(root.get(0));  //putting the root folder in front
                folders.remove(index);
                folders.add(0, root.get(0));

                application.setLayerDrawer(mDrawerLayout);

            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return folders;
        }


    }

    protected class GetByLayersFolderTask extends GeoAsyncTask<Integer, Void, Folder> {

        public GetByLayersFolderTask(GetLayersByFolderTaskParams params) {
            super(params.getFragment());
        }

        @Override
        protected Folder doInBackground(Integer... params) {
            Folder currentFolder = null;

            try {

                List<Folder> folders = mTreeService.getLayers();

                currentFolder = folders.get(0);

                getCurrentFolderLayers(currentFolder);

                if(params[0] != 0) {
                    currentFolder = mFolderTreeService.getById(params[0]);
                }

                currentFolder.setFolders(mFolderTreeService.getFoldersByFolder(currentFolder.getId(), false));
                currentFolder.setLayers(mFolderTreeService.getLayersByFolder(currentFolder.getId(), false));
            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return currentFolder;
        }

        private void getCurrentFolderLayers(Folder currentFolder) {
            List<Folder> allFolders = mDataHelper.getFoldersRecursively(currentFolder, currentFolder.getParent());
            List<Layer> allLayers = mLayerService.getLayers();

            if (allFolders.size() > 0) {
                mFolderRepo.Add(allFolders);
            }
            else { Log.d(TAG, "allFolders empty."); }
            if (allLayers.size() > 0) {
                mLayerRepo.Add(allLayers);
            }
            else { Log.d(TAG, "allLayers empty."); }
        }
    }
}
