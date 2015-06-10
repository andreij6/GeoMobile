package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataHelper {

    public ArrayList<Folder> getFoldersRecursively(Folder folder, Folder parentFolder) {
        ArrayList<Folder> result = new ArrayList<>();

        if (folder == null) return result;

        if (folder.getFolders().size() == 0) {
            setParentFolder(folder, parentFolder);

            result.add(folder);
        } else {
            for (Folder x : folder.getFolders()) {
                result.addAll(getFoldersRecursively(x, folder));
            }

            if (!result.contains(folder)) {
                setParentFolder(folder, parentFolder);

                result.add(folder);
            }
        }
        return result;
    }

    protected void setParentFolder(Folder folder, Folder parentFolder){
        if(parentFolder != null) folder.setParent(parentFolder);
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

    public ArrayList<Document> getDocumentsRecursively(Folder folder) {
        ArrayList<Document> result = new ArrayList<>();

        if (folder == null) return result;

        if (folder.getFolders().size() == 0) {
            result.addAll(folder.getDocuments());
        } else {
            for (Folder x : folder.getFolders()) {
                result.addAll(getDocumentsRecursively(x));
            }
        }

        return result;
    }

    public List<ListItem> CombineLayerItems(List<Layer> layers, List<Folder> folders, Folder parent) {
        List<Folder> infolders = folders != null ? folders : new ArrayList<Folder>();

        ArrayList<ListItem> results = new ArrayList<>();

        if (parent != null) {
            ListItem listItem = new ListItem(parent);
            listItem.setIconId(R.drawable.ic_arrow_left_bold_black_24dp);
            listItem.setName("");
            listItem.setShowInfoIcon(false);
            results.add(listItem);
        }

        if (infolders != null) {
            for (Folder folder : infolders) {
                ListItem listItem = new ListItem(folder);
                results.add(listItem);
            }

            SetupEmptyFolder(infolders, results);
        }

        if (layers != null) {
            for (Layer layer : layers) {
                ListItem listItem = new ListItem(layer);
                results.add(listItem);
            }

            if(layers.isEmpty()){
                ListItem l = new ListItem(new Layer(), true);
                results.add(l);
            }
        }

        Collections.sort(results);

        return results;
    }

    public List<ListItem> CombineLibraryItems(List<Document> documents, List<Folder> folders, Folder parent) {
        List<Folder> infolders = folders != null ? folders : new ArrayList<Folder>();

        ArrayList<ListItem> results = new ArrayList<>();

        if (parent != null) {
            ListItem listItem = new ListItem(parent);
            listItem.setIconId(R.drawable.ic_arrow_left_bold_black_24dp);
            listItem.setName("");
            listItem.setShowInfoIcon(false);
            results.add(listItem);
        }

        if (infolders != null) {
            for (Folder folder : infolders) {
                ListItem listItem = new ListItem(folder);
                results.add(listItem);
            }

            SetupEmptyFolder(infolders, results);
        }

        if (documents != null) {
            for (Document document : documents) {
                ListItem listItem = new ListItem(document);

                results.add(listItem);
            }

            if(documents.isEmpty()){
                ListItem listItem = new ListItem(new Document(), true);
                results.add(listItem);
            }
        }

        Collections.sort(results);

        return results;
    }

    protected void SetupEmptyFolder(List<Folder> folders, ArrayList<ListItem> results) {
        if(folders.isEmpty()){
            ListItem listItem = new ListItem(new Folder(), true);
            results.add(listItem);
        }
    }
}
