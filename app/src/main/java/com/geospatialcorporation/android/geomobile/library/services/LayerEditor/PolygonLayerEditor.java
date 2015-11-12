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
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Ring;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PolygonLayerEditor extends LayerEditorBase<Polygon> {

    List<Marker> mPolyMarkers;
    Polygon mCreatingPolygon;

    private static final int POLYGON = GeometryTypeCodes.Polygon;

    public PolygonLayerEditor(LegendLayer legendLayer, GoogleMap map, Context context) {
        super(legendLayer, map, context);
        mPolyMarkers = new ArrayList<>();
        mCreatingPolygon = null;
    }

    @Override
    public void setUndoRedoListeners(FloatingActionButton undoBtn, FloatingActionButton redoBtn) {
        super.setUndoRedoListeners(undoBtn, redoBtn);

        mExistingFeatures = mLayerManager.getVisiblePolygons();

        //TODO: CHANGE REQUEST

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mEdits.size();

                //mMapFragment.clearHighlights();

                if(size != 0){
                    HashMap<Integer, Polygon> toUndoMap = mEdits.get(size - 1);

                    if(isAction(toUndoMap, EditLayerActionCodes.ADD_POLYGON)){
                        Polygon toUndo = toUndoMap.get(EditLayerActionCodes.ADD_POLYGON);

                        toUndo.remove();

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Polygon> actionPolygon = makeActionMap(toUndo, EditLayerActionCodes.ADD_POLYGON);

                        mChangeMap.remove(toUndo.getId());

                        mUndos.add(actionPolygon);
                    }

                    if(isAction(toUndoMap, EditLayerActionCodes.DELETE_POLYGON)){
                        Polygon toUndo = toUndoMap.get(EditLayerActionCodes.DELETE_POLYGON);

                        Polygon polygon = mMap.addPolygon(
                                new PolygonOptions()
                                        .addAll(toUndo.getPoints())
                                        .geodesic(true)
                                        .fillColor(Color.LTGRAY)
                                        .strokeColor(Color.DKGRAY)
                                        .strokeWidth(10));

                        mEdits.remove(toUndoMap);

                        HashMap<Integer, Polygon> actionPolygon = makeActionMap(polygon, EditLayerActionCodes.DELETE_POLYGON);

                        mUndos.add(actionPolygon);
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
                    HashMap<Integer, Polygon> toRedoMap = mUndos.get(size - 1);

                    if (isAction(toRedoMap, EditLayerActionCodes.ADD_POLYGON)) {
                        Polygon toRedo = toRedoMap.get(EditLayerActionCodes.ADD_POLYGON);

                        Polygon polygon = mMap.addPolygon(
                                new PolygonOptions()
                                        .addAll(toRedo.getPoints())
                                        .geodesic(true)
                                        .fillColor(Color.LTGRAY)
                                        .strokeColor(Color.DKGRAY)
                                        .strokeWidth(10));

                        HashMap<Integer, Polygon> actionPolygon = makeActionMap(polygon, EditLayerActionCodes.ADD_POLYGON);

                        mEdits.add(actionPolygon);

                        mUndos.remove(toRedoMap);
                    }

                    if (isAction(toRedoMap, EditLayerActionCodes.DELETE_POLYGON)) {
                        Polygon toRedo = toRedoMap.get(EditLayerActionCodes.DELETE_POLYGON);

                        toRedo.remove();

                        HashMap<Integer, Polygon> actionPolygon = makeActionMap(toRedo, EditLayerActionCodes.DELETE_POLYGON);

                        mEdits.add(actionPolygon);

                        mUndos.remove(toRedoMap);
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
                Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

                searchAndRemove(polygons, latLng);
                searchAndRemove(getCreatedFeatures(mUndos), latLng);
                searchAndRemove(getCreatedFeatures(mEdits), latLng);
            }
        });
    }

    private void searchAndRemove(Iterable<Polygon> polygons, LatLng latLng) {
        for (Polygon ss : polygons) {
            if (PolyUtil.containsLocation(latLng, ss.getPoints(), true)) {
                ss.remove();

                HashMap<Integer, Polygon> deletedPolygon = makeActionMap(ss, EditLayerActionCodes.DELETE_POLYGON);

                mEdits.add(deletedPolygon);

                if(isExisting(ss)){
                    String mapFeatureId = mLayerManager.getFeatureId(ss.getId(), LayerManager.POLYGON);

                    Change change = new Change.Factory()
                            .Create(Change.REMOVE, mLayer.getId(), null,
                                    new PolygonGeometry(ss), mapFeatureId, POLYGON);

                    mChangeMap.put(ss.getId(), change);
                } else {
                    if(mChangeMap.containsKey(ss.getId())) {
                        mChangeMap.remove(ss.getId());
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

                if (mPolyMarkers.size() == 2) {
                    Toast.makeText(mContext, "Press & Hold to Finish Polygon", Toast.LENGTH_SHORT).show();
                }

                if (mPolyMarkers.size() > 2) {
                    createPolygon();
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
                                .anchor(0.5f, 0.5f));

                mPolyMarkers.add(marker);

                if (mPolyMarkers.size() > 2) {
                    removeCreatingPolygon();

                    Polygon polygon = mMap.addPolygon(
                            new PolygonOptions()
                                    .addAll(getLinePositions(mPolyMarkers))
                                    .geodesic(true)
                                    .fillColor(Color.LTGRAY)
                                    .strokeColor(Color.DKGRAY)
                                    .strokeWidth(10));

                    HashMap<Integer, Polygon> actionMarker = makeActionMap(polygon, EditLayerActionCodes.ADD_POLYGON);

                    mEdits.add(actionMarker);

                    removeMarkers(mPolyMarkers);

                    Change change = new Change.Factory()
                            .Create(Change.ADD, mLayer.getId(),
                                    new PolygonGeometry(polygon), null, "-1", POLYGON);

                    mChangeMap.put(polygon.getId(), change);

                    mPolyMarkers.clear();
                }
            }
        });
    }
/*
    "GeometryTypeCode":3,
            "Rings":
            [
                {
                    "GeometryTypeCode":2,
                        "Points":
                    [
                        {"GeometryTypeCode":1,"X":-91.16455078125,"Y":35.52104976129943},
                        {"GeometryTypeCode":1,"X":-90.90362548828125,"Y":35.36665566526249},
                        {"GeometryTypeCode":1,"X":-90.57403564453125,"Y":35.529991058953534},
                        {"GeometryTypeCode":1,"X":-90.7086181640625,"Y":35.655064568953875},
                        {"GeometryTypeCode":1,"X":-91.16455078125,"Y":35.52104976129943}
                    ],
                    "PointCount":4
                }
        ],
            "PointOrder":2
*/

    private void createPolygon() {
        removeCreatingPolygon();

        mCreatingPolygon = mMap.addPolygon(
                new PolygonOptions().addAll(getLinePositions(mPolyMarkers))
                        .geodesic(true)
                        .strokeColor(Color.DKGRAY)
                        .strokeWidth(10));
    }

    @Override
    public void resetMapFragment() {
        removeMarkers(mPolyMarkers);

        removeCreatingPolygon();

        removePolygons(getCreatedFeatures(mEdits));
        removePolygons(getCreatedFeatures(mUndos));

        removePolygons(mExistingFeatures);
    }

    @Override
    protected boolean isExisting(Polygon feature) {
        mExistingFeatures = mLayerManager.getVisiblePolygons();

        for (Polygon m : mExistingFeatures) {
            if(m.getId().equals(feature.getId())){
                return true;
            }

            if(m.getPoints().equals(feature.getPoints())){
                return true;
            }
        }

        return  false;
    }

    private void removePolygons(Iterable<Polygon> polygons){
        if(polygons != null){
            for (Polygon p : polygons){
                p.remove();
            }
        }
    }

    private void removeCreatingPolygon(){
        if (mCreatingPolygon != null) {
            mCreatingPolygon.remove();
        }
    }



}
