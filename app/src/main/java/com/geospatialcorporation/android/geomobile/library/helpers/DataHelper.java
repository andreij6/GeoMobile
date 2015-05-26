package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataHelper {

    public ArrayList<Folder> getFoldersRecursively(Folder folder) {
        ArrayList<Folder> result = new ArrayList<>();

        if (folder == null) return result;

        if (folder.getFolders().size() == 0) {
            result.add(folder);
        } else {
            for (Folder x : folder.getFolders()) {
                result.addAll(getFoldersRecursively(x));
            }

            if (!result.contains(folder)) {
                result.add(folder);
            }
        }
        return result;
    }

    public ArrayList<Layer> getLayersRecursively(Folder folder) {
        ArrayList<Layer> result = new ArrayList<>();

        if (folder == null) return result;

        if (folder.getFolders().size() == 0) {
            result.addAll(folder.getLayers());
        } else {

            for (Folder x : folder.getFolders()) {
                result.addAll(getLayersRecursively(x));
            }
        }
        return result;
    }

    public List<ListItem> CombineLayerItems(List<Layer> layers, List<Folder> folders, Folder parent) {
        ArrayList<ListItem> results = new ArrayList<>();

        if (parent != null) {
            ListItem listItem = new ListItem(parent);
            results.add(listItem);
        }

        if (folders != null) {
            for (Folder folder : folders) {
                ListItem listItem = new ListItem(folder);
                results.add(listItem);
            }
        }

        if (layers != null) {
            for (Layer layer : layers) {
                ListItem listItem = new ListItem(layer);
                results.add(listItem);
            }
        }

        Collections.sort(results);

        return results;
    }

    public List<ListItem> CombineLibraryItems(List<Document> documents, List<Folder> folders, Folder parent) {
        ArrayList<ListItem> results = new ArrayList<>();

        if (parent != null) {
            ListItem listItem = new ListItem(parent);
            results.add(listItem);
        }

        if (folders != null) {
            for (Folder folder : folders) {
                ListItem listItem = new ListItem(folder);
                results.add(listItem);
            }
        }

        if (documents != null) {
            for (Document document : documents) {
                ListItem listItem = new ListItem(document);

                results.add(listItem);
            }
        }

        Collections.sort(results);

        return results;
    }
}
