package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import java.util.ArrayList;
import java.util.List;

public class EditChangeRequest {

    public EditChangeRequest(){
        Changes = new ArrayList<>();
    }

    Iterable<Change> Changes;

    public Iterable<Change> getChanges() {
        return Changes;
    }

    public void setChanges(Iterable<Change> changes) {
        Changes = changes;
    }
}


