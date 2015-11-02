package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.constants.EditLayerActionCodes;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LineLayerEditor extends LayerEditorBase<Polyline>  {

    Polyline mCreatingLine;
    List<Marker> mPolyMarkers;


    public LineLayerEditor(LegendLayer legendLayer, GoogleMap map, Context context) {
        super(legendLayer, map, context);
        mPolyMarkers = new ArrayList<>();
    }

    @Override
    public void setUndoRedoListeners(FloatingActionButton undoBtn, FloatingActionButton redoBtn) {
        super.setUndoRedoListeners(undoBtn, redoBtn);

        mExistingFeatures = mLayerManager.getVisiblePolylines();

        //TODO: CHANGE REQUEST

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mEdits.size();

                //mMapFragment.clearHighlights();

                if (size != 0) {
                    HashMap<Integer, Polyline> toUndoMap = mEdits.get(size - 1);

                    if (isAction(toUndoMap, EditLayerActionCodes.ADD_LINE)) {
                        Polyline toUndo = toUndoMap.get(EditLayerActionCodes.ADD_LINE);

                        toUndo.remove();

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Polyline> actionLine = makeActionMap(toUndo, EditLayerActionCodes.ADD_LINE);

                        mChangeMap.remove(toUndo.getId());

                        mUndos.add(actionLine);
                    }

                    if (isAction(toUndoMap, EditLayerActionCodes.DELETE_LINE)) {
                        Polyline toUndo = toUndoMap.get(EditLayerActionCodes.DELETE_LINE);

                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .addAll(toUndo.getPoints())
                                .geodesic(true)
                                .color(Color.BLUE)
                                .width(10));

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Polyline> actionLine = makeActionMap(line, EditLayerActionCodes.DELETE_LINE);

                        mUndos.add(actionLine);

                        //if line isExisting remove it

                        //else add new change request
                        if(isExisting(toUndo)){
                            mChangeMap.remove(toUndo.getId());
                        } else {
                            Change change = new Change.Factory()
                                    .Create(Change.ADD, mLayer.getId(),
                                            getLine(line), null, "-1", GeometryTypeCodes.Line);

                            mChangeMap.put(line.getId(), change);
                        }
                    }
                }
            }
        });

        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mUndos.size();

                //mMap.clearHighlights();

                if (size != 0) {
                    HashMap<Integer, Polyline> toRedoMap = mUndos.get(size - 1);

                    if (isAction(toRedoMap, EditLayerActionCodes.ADD_LINE)) {
                        Polyline toRedo = toRedoMap.get(EditLayerActionCodes.ADD_LINE);

                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .addAll(toRedo.getPoints())
                                .geodesic(true)
                                .color(Color.BLUE)
                                .width(10));

                        HashMap<Integer, Polyline> actionMarker = makeActionMap(line, EditLayerActionCodes.ADD_LINE);

                        mEdits.add(actionMarker);

                        mUndos.remove(toRedoMap);

                        Change change = new Change.Factory()
                                .Create(Change.ADD, mLayer.getId(),
                                        getLine(line), null, "-1", GeometryTypeCodes.Line);

                        mChangeMap.put(line.getId(), change);
                    }

                    if (isAction(toRedoMap, EditLayerActionCodes.DELETE_LINE)) {
                        Polyline toRedo = toRedoMap.get(EditLayerActionCodes.DELETE_LINE);

                        toRedo.remove();

                        HashMap<Integer, Polyline> actionMarker = makeActionMap(toRedo, EditLayerActionCodes.DELETE_LINE);

                        mEdits.add(actionMarker);

                        mUndos.remove(toRedoMap);

                        if(isExisting(toRedo)){
                            String mapFeatureId = mLayerManager.getFeatureId(toRedo.getId(), LayerManager.LINE);

                            Change change = new Change.Factory()
                                    .Create(Change.REMOVE, mLayer.getId(), null,
                                            getLine(toRedo), mapFeatureId, GeometryTypeCodes.Line);

                            mChangeMap.put(toRedo.getId(), change);
                        } else {
                            if(mChangeMap.containsKey(toRedo.getId())) {
                                mChangeMap.remove(toRedo.getId());
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void moveClickListener() {
        super.moveClickListener();

    }

    @Override
    public void removeClickListener() {
        super.removeClickListener();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();
                List<Polyline> created = getCreatedFeatures(mEdits);
                List<Polyline> visibleUndone = getCreatedFeatures(mUndos);

                searchAndRemove(lines, latLng);
                searchAndRemove(created, latLng);
                searchAndRemove(visibleUndone, latLng);
            }
        });
    }

    private void searchAndRemove(Iterable<Polyline> lines, LatLng latLng) {
        double tolerance = mMapFragment.calculateTolerance(mMap.getCameraPosition().zoom);

        for (Polyline line : lines) {
            if (PolyUtil.isLocationOnPath(latLng, line.getPoints(), false, tolerance)) { //idea: reset tolerance by zoom level

                line.remove();

                HashMap<Integer, Polyline> deletedPolyline = makeActionMap(line, EditLayerActionCodes.DELETE_LINE);

                mEdits.add(deletedPolyline);

                if(isExisting(line)){
                    String mapFeatureId = mLayerManager.getFeatureId(line.getId(), LayerManager.LINE);

                    Change change = new Change.Factory()
                            .Create(Change.REMOVE, mLayer.getId(), null,
                                    getLine(line), mapFeatureId, GeometryTypeCodes.Line);

                    mChangeMap.put(line.getId(), change);
                } else {
                    if(mChangeMap.containsKey(line.getId())) {
                        mChangeMap.remove(line.getId());
                    }
                }

                break;
            }
        }
    }

    @Override
    public void addClickListener() {
        super.addClickListener();
        
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_edit_marker_orange))
                                .anchor(0.5f, 0.5f));

                mPolyMarkers.add(marker);

                if (mPolyMarkers.size() == 1) {
                    Toast.makeText(mContext, "Press & Hold Finish Line", Toast.LENGTH_SHORT).show();
                }

                if (mPolyMarkers.size() > 1) {
                    createALine();
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_edit_marker_orange))
                                .anchor(-0.5f, -0.5f));

                mPolyMarkers.add(marker);

                if (mPolyMarkers.size() > 1) {
                    removeCreatingLine();

                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .addAll(getLinePositions(mPolyMarkers))
                            .geodesic(true)
                            .color(Color.BLUE)
                            .width(10));

                    HashMap<Integer, Polyline> actionMarker = makeActionMap(line, EditLayerActionCodes.ADD_LINE);

                    mEdits.add(actionMarker);

                    removeMarkers(mPolyMarkers);

                   Change change = new Change.Factory()
                           .Create(Change.ADD, mLayer.getId(),
                                   getLine(line), null, "-1", GeometryTypeCodes.Line);

                   mChangeMap.put(line.getId(), change);

                    mPolyMarkers.clear();
                }
            }
        });
    }

    private Geometry getLine(Polyline line) {
        Geometry result = new Geometry();

        result.setGeometryTypeCode(GeometryTypeCodes.Line);

        List<Geometry> points = new ArrayList<>();

        for (LatLng point : line.getPoints()) {
            points.add(new Geometry(point));
        }

        result.setPoints(points);
        result.setPointCount(points.size());

        return result;
    }

    /*
    "Arguments":
    {
        "TypeCode":2,
            "MapFeature":
        {
            "GeometryTypeCode":2,
                "Points":
            [
            {"GeometryTypeCode":1,"X":-91.5655517578125,"Y":35.22542873333704},
            {"GeometryTypeCode":1,"X":-91.07666015625,"Y":34.96699890670367},
            {"GeometryTypeCode":1,"X":-90.64544677734375,"Y":35.19625600786368}
            ],
            "PointCount":3
        },
        "OldFeature":null,
            "LayerId":7642,
            "MapFeatureId":-1
    }
    */

    private void createALine() {
        removeCreatingLine();

        mCreatingLine = mMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(getLinePositions(mPolyMarkers))
                                    .geodesic(true)
                                    .color(Color.BLUE)
                                    .width(10));
    }

    @Override
    public void resetMapFragment() {
        removeMarkers(mPolyMarkers);

        removeCreatingLine();

        removeLines(getCreatedFeatures(mEdits));
        removeLines(getCreatedFeatures(mUndos));

        removeLines(mExistingFeatures);
    }

    @Override
    protected boolean isExisting(Polyline feature) {
        mExistingFeatures = mLayerManager.getVisiblePolylines();

        for (Polyline m : mExistingFeatures) {
            if(m.getId().equals(feature.getId())){
                return true;
            }

            if(m.getPoints().equals(feature.getPoints())){
                return true;
            }
        }

        return  false;
    }

    private void removeLines(Iterable<Polyline> lines) {
        if(lines != null){
            for (Polyline line : lines) {
                line.remove();
            }
        }
    }

    private void removeCreatingLine() {
        if (mCreatingLine != null) {
            mCreatingLine.remove();
        }
    }

}
