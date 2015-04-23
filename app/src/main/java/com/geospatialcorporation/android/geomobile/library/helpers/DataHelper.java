package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andre on 4/22/2015.
 */
public class DataHelper {

    public ArrayList<Folder> GetFoldersRecursively(Folder folder) {
        ArrayList<Folder> result = new ArrayList<>();

        if(folder.getFolders().size() == 0){
            result.add(folder);
        } else {
            for(Folder x : folder.getFolders()){
                result.addAll(GetFoldersRecursively(x));
            }

            if(!result.contains(folder)){
                result.add(folder);
            }
        }
        return result;
    }

    public ArrayList<Layer> GetLayersRecursively(Folder folder) {
        ArrayList<Layer> result = new ArrayList<>();

        if(folder.getFolders().size() == 0){
            result.addAll(folder.getLayers());
        } else {

            for(Folder x : folder.getFolders()){
                result.addAll(GetLayersRecursively(x));
            }
        }
        return result;
    }

    public List<ListItem> CombineLibraryItems(List<Layer> layers, List<Folder> folders){
        ArrayList<ListItem> results = new ArrayList<>();

        if(folders != null) {
            for (Folder folder : folders) {
                ListItem listItem = new ListItem(folder);

                results.add(listItem);
            }
        }

        if(layers != null) {
            for (Layer layer : layers) {
                ListItem listItem = new ListItem(layer);

                results.add(listItem);
            }
        }

        Collections.sort(results);

        return results;
    }
}
