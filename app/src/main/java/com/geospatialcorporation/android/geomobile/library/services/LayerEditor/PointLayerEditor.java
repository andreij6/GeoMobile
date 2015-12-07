package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import android.content.Context;
import android.view.View;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.constants.EditLayerActionCodes;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointLayerEditor extends LayerEditorBase<Marker> {

    List<LatLng> mMarkerMovedPosition;
    List<LatLng> mUndoneMarkerMovedPosition;
    Marker mSelectedMoveMarker;

    HashMap<String, LatLng> mOriginalPositionMap;


    public PointLayerEditor(LegendLayer legendLayer, GoogleMap map, Context context) {
        super(legendLayer, map, context);
        mMapFragment = application.getMapFragment();
        mMarkerMovedPosition = new ArrayList<>();
        mUndoneMarkerMovedPosition = new ArrayList<>();
        mSelectedMoveMarker = null;
        mOriginalPositionMap = new HashMap<>();
    }

    @Override
    public void setUndoRedoListeners(FloatingActionButton undoBtn, FloatingActionButton redoBtn) {

        //region Undo
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mEdits.size();

                mMapFragment.clearHighlights();

                if (size != 0) {
                    HashMap<Integer, Marker> toUndoMap = mEdits.get(size - 1);

                    if (isAction(toUndoMap, EditLayerActionCodes.ADD_MARKER)) {
                        Marker toUndo = toUndoMap.get(EditLayerActionCodes.ADD_MARKER);

                        toUndo.remove();

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Marker> actionMarker = makeActionMap(toUndo, EditLayerActionCodes.ADD_MARKER);

                        mChangeMap.remove(toUndo.getId());

                        mUndos.add(actionMarker);
                    }

                    if (isAction(toUndoMap, EditLayerActionCodes.MOVE_MARKER)) {
                        Marker toUndo = toUndoMap.get(EditLayerActionCodes.MOVE_MARKER);

                        int movedSize = mMarkerMovedPosition.size();

                        LatLng movedPosition = mMarkerMovedPosition.get(movedSize - 1);

                        LatLng preMovedPosition = toUndo.getPosition();

                        toUndo.setPosition(movedPosition);

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Marker> actionMarker = makeActionMap(toUndo, EditLayerActionCodes.MOVE_MARKER);

                        mUndos.add(actionMarker);

                        mUndoneMarkerMovedPosition.add(preMovedPosition);

                        if(isExisting(toUndo)){
                            LatLng origPosition = mOriginalPositionMap.get(toUndo.getId());

                            if(movedPosition.equals(origPosition)){
                                mChangeMap.remove(toUndo.getId());
                            } else {
                                Change change = mChangeMap.get(toUndo.getId());

                                change.updateMapFeature(new Geometry(movedPosition));
                            }
                        } else {
                            Change change = mChangeMap.get(toUndo.getId());

                            change.updateMapFeature(new Geometry(movedPosition));
                        }

                        mMarkerMovedPosition.remove(movedPosition);
                    }

                    if (isAction(toUndoMap, EditLayerActionCodes.REMOVE_MARKER)) {
                        Marker toUndo = toUndoMap.get(EditLayerActionCodes.REMOVE_MARKER);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(toUndo.getPosition()).icon(mLegendLayer.getBitmap()));

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Marker> actionMarker = makeActionMap(marker, EditLayerActionCodes.REMOVE_MARKER);

                        mUndos.add(actionMarker);

                        if(isExisting(toUndo) && mOriginalPositionMap.get(toUndo.getId()) == null){
                            mChangeMap.remove(toUndo.getId());
                        } else if(isExisting(toUndo) && mOriginalPositionMap.get(toUndo.getId()) != null){
                            String mapFeatureId = mLayerManager.getFeatureId(toUndo.getId(), LayerManager.POINT);

                            Change change = new Change.Factory()
                                    .Create(Change.MOVE, mLayer.getId(), null,
                                            new Geometry(toUndo.getPosition()), mapFeatureId, GeometryTypeCodes.Point);

                            mChangeMap.put(toUndo.getId(), change);
                        } else {
                            Change change = new Change.Factory()
                                    .Create(Change.ADD, mLayer.getId(),
                                            new Geometry(marker.getPosition()), null, "-1", GeometryTypeCodes.Point);

                            mChangeMap.put(marker.getId(), change);
                        }
                    }

                }
            }
        });
        //endregion

        //region Redo
        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mUndos.size();

                mMapFragment.clearHighlights();

                if (size != 0) {
                    HashMap<Integer, Marker> toRedoMap = mUndos.get(size - 1);

                    if (isAction(toRedoMap, EditLayerActionCodes.ADD_MARKER)) {
                        Marker toRedo = toRedoMap.get(EditLayerActionCodes.ADD_MARKER);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(toRedo.getPosition()).icon(mLegendLayer.getBitmap()));

                        HashMap<Integer, Marker> actionMarker = makeActionMap(marker, EditLayerActionCodes.ADD_MARKER);

                        mEdits.add(actionMarker);

                        mUndos.remove(toRedoMap);

                        Change change = new Change.Factory()
                                .Create(Change.ADD, mLayer.getId(), new Geometry(marker.getPosition()), null, "-1", GeometryTypeCodes.Point);

                        mChangeMap.put(marker.getId(), change);
                    }

                    if (isAction(toRedoMap, EditLayerActionCodes.MOVE_MARKER)) {
                        Marker toRedo = toRedoMap.get(EditLayerActionCodes.MOVE_MARKER);

                        int undoneMarkerSize = mUndoneMarkerMovedPosition.size();

                        LatLng position = mUndoneMarkerMovedPosition.get(undoneMarkerSize - 1);

                        LatLng movedPosition = toRedo.getPosition();

                        toRedo.setPosition(position);

                        HashMap<Integer, Marker> actionMarker = makeActionMap(toRedo, EditLayerActionCodes.MOVE_MARKER);

                        mEdits.add(actionMarker);

                        mUndos.remove(toRedoMap);

                        mUndoneMarkerMovedPosition.remove(undoneMarkerSize - 1);

                        mMarkerMovedPosition.add(movedPosition);

                        if (isExisting(toRedo) && !mChangeMap.containsKey(toRedo.getId())) {
                            String mapFeatureId = mLayerManager.getFeatureId(toRedo.getId(), LayerManager.POINT);

                            Change change = new Change.Factory()
                                    .Create(Change.MOVE, mLayer.getId(), null,
                                            new Geometry(toRedo.getPosition()), mapFeatureId, GeometryTypeCodes.Point);

                            mChangeMap.put(toRedo.getId(), change);
                        } else {
                            //the marker is a create Argument just update the position
                            if(!isExisting(toRedo) && mChangeMap.containsKey(toRedo.getId())) {
                                Change change = mChangeMap.get(toRedo.getId());

                                change.updateMapFeature(new Geometry(toRedo.getPosition()));
                            }
                        }
                    }

                    if (isAction(toRedoMap, EditLayerActionCodes.REMOVE_MARKER)) {
                        Marker toRedo = toRedoMap.get(EditLayerActionCodes.REMOVE_MARKER);

                        toRedo.remove();

                        HashMap<Integer, Marker> actionMarker = makeActionMap(toRedo, EditLayerActionCodes.REMOVE_MARKER);

                        mEdits.add(actionMarker);

                        mUndos.remove(toRedoMap);

                        if (isExisting(toRedo)) {

                            String mapFeatureId = mLayerManager.getFeatureId(toRedo.getId(), LayerManager.POINT);

                            Change change = new Change.Factory()
                                    .Create(Change.REMOVE, mLayer.getId(), null,
                                            new Geometry(toRedo.getPosition()), mapFeatureId, GeometryTypeCodes.Point);

                            mChangeMap.put(toRedo.getId(), change);
                        } else {
                            mChangeMap.remove(toRedo.getId());
                        }
                    }
                }
            }
        });
        //endregion
    }

    @Override
    public void moveClickListener() {
        super.moveClickListener();

        mExistingFeatures = mLayerManager.getVisibleMarkersByLayerId(mLegendLayer);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (mSelectedMoveMarker != null) {
                    if (mSelectedMoveMarker.getId() != marker.getId()) {
                        mMapFragment.clearHighlights();
                        mSelectedMoveMarker = marker;
                        mMapFragment.highlight(marker, false);
                    }
                } else {
                    mMapFragment.clearHighlights();
                    mSelectedMoveMarker = marker;
                    mMapFragment.highlight(marker, false);
                }

                if(isExisting(mSelectedMoveMarker) && !mOriginalPositionMap.containsKey(mSelectedMoveMarker.getId())){
                    LatLng existingOriginalPosition = mSelectedMoveMarker.getPosition();

                    mOriginalPositionMap.put(mSelectedMoveMarker.getId(), existingOriginalPosition);
                }

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mSelectedMoveMarker != null) {
                    mMapFragment.clearHighlights();
                    LatLng oldPosition = mSelectedMoveMarker.getPosition();
                    mMarkerMovedPosition.add(oldPosition);
                    mSelectedMoveMarker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
                    mMapFragment.highlight(mSelectedMoveMarker, false);

                    HashMap<Integer, Marker> movedMarker = makeActionMap(mSelectedMoveMarker, EditLayerActionCodes.MOVE_MARKER);

                    mEdits.add(movedMarker);

                    if (isExisting(mSelectedMoveMarker)) {
                        String mapFeatureId = mLayerManager.getFeatureId(mSelectedMoveMarker.getId(), LayerManager.POINT);

                        Change change = new Change.Factory()
                                .Create(Change.MOVE, mLayer.getId(),
                                        new Geometry(mSelectedMoveMarker.getPosition()), new Geometry(oldPosition), mapFeatureId,
                                        GeometryTypeCodes.Point);

                        mChangeMap.put(mSelectedMoveMarker.getId(), change);
                    } else {
                        //the marker is a create Argument just update the position
                        Change change = mChangeMap.get(mSelectedMoveMarker.getId());

                        if (change != null) {
                            change.updateMapFeature(new Geometry(mSelectedMoveMarker.getPosition()));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void removeClickListener() {
        super.removeClickListener();

        mMapFragment.clearHighlights();

        mExistingFeatures = mLayerManager.getVisibleMarkersByLayerId(mLegendLayer);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();

                HashMap<Integer, Marker> movedMarker = makeActionMap(marker, EditLayerActionCodes.REMOVE_MARKER);

                mEdits.add(movedMarker);

                if (isExisting(marker)) {

                    String mapFeatureId = mLayerManager.getFeatureId(marker.getId(), LayerManager.POINT);

                    Change change = new Change.Factory()
                            .Create(Change.REMOVE, mLayer.getId(), null,
                                    new Geometry(marker.getPosition()), mapFeatureId, GeometryTypeCodes.Point);

                    mChangeMap.put(marker.getId(), change);
                } else {
                    if (mChangeMap.containsKey(marker.getId())) {
                        mChangeMap.remove(marker.getId());
                    }
                }

                return false;
            }
        });
    }

    protected boolean isExisting(Marker marker) {

        for (Marker m : mExistingFeatures) {
            if(m.getId().equals(marker.getId())){
                return true;
            }
        }

        return  false;
    }

    @Override
    public void addClickListener() {
       super.addClickListener();

        mMapFragment.clearHighlights();

        mExistingFeatures = mLayerManager.getVisibleMarkersByLayerId(mLegendLayer);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(mLegendLayer.getBitmap())
                                .anchor(0.5f, 0.5f)
                );

                HashMap<Integer, Marker> actionMarker = makeActionMap(marker, EditLayerActionCodes.ADD_MARKER);

                mEdits.add(actionMarker);

                Change change = new Change.Factory()
                        .Create(Change.ADD, mLayer.getId(), new Geometry(marker.getPosition()), null, "-1", GeometryTypeCodes.Point);

                mChangeMap.put(marker.getId(), change);
            }
        });
    }

    @Override
    public void resetMapFragment() {
        for(HashMap<Integer, Marker> map : mEdits){
            removeMarkers(map.values());
        }

        for(HashMap<Integer, Marker> map : mUndos){
            removeMarkers(map.values());
        }

        mExistingFeatures = mLayerManager.getVisibleMarkersByLayerId(mLegendLayer);

        removeMarkers(mExistingFeatures);
    }


}
