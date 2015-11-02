package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import com.melnykov.fab.FloatingActionButton;

public interface ILayerEditor {
    void setUndoRedoListeners(FloatingActionButton undoBtn, FloatingActionButton redoBtn);

    void moveClickListener();

    void removeClickListener();

    void addClickListener();

    void resetMapFragment();

    void saveEdits();
}
