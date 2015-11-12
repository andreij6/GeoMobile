package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import java.util.ArrayList;
import java.util.List;

public class EditChangeRequest {

    public EditChangeRequest(){
        Changes = new ArrayList<>();
    }

    List<Change> Changes;

    public List<Change> getChanges() {
        return Changes;
    }

    public void setChanges(Iterable<Change> changes) {

        for (Change change : changes) {
            Changes.add(change);
        }
    }
}


