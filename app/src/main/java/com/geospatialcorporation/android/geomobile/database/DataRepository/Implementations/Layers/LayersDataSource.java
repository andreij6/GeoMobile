package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.DataSourceGenericBase;
import com.geospatialcorporation.android.geomobile.database.sqlitehelper.GeoSQLiteHelper;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;

public class LayersDataSource extends DataSourceGenericBase<Layer> {

    //region Constructor
    public LayersDataSource(Context context) {
        super(context);

        GetEntityById = GetLayersById;
        GetAllEntities = GetAllLayers;
        UpdateEntity = UpdateLayer;
        CreateEntity = CreateLayer;
        CreateMultiple = CreateMultipleLayers;
        mTableName = GeoSQLiteHelper.LAYER_TABLE;
    }
    //endregion

    //region Action Helpers
    protected DbAction GetLayersById = new DbAction(){
        @Override
        public void Run(){
            Cursor cursor = RawGetById(GeoSQLiteHelper.COLUMN_LAYER_LAYERID);

            if(cursor.moveToFirst()){
                SetLayerProperties(mEntity, cursor);
            }
        }
    };

    protected DbAction GetAllLayers = new DbAction(){
        @Override
        public void Run(){
            ArrayList<Layer> layers = new ArrayList<>();

            Cursor cursor = GetAllCursor();

            //region QueryService Example
                    /*
                    mDatabase.query(
                    GeoSQLiteHelper.LAYER_TABLE,
                    new String[]{
                            BaseColumns._ID,
                            GeoSQLiteHelper.COLUMN_LAYER_NAME,
                            GeoSQLiteHelper.COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID,
                            GeoSQLiteHelper.COLUMN_LAYER_IS_SHOWING,
                            GeoSQLiteHelper.COLUMN_LAYER_IS_FIXED,
                            GeoSQLiteHelper.COLUMN_LAYER_IS_OWNER,
                            GeoSQLiteHelper.COLUMN_LAYER_STYLEPATH,
                            GeoSQLiteHelper.COLUMN_LAYER_EXTENT
                    },
                    null, //Selection
                    null, //selectionArgs
                    null, //groupBy
                    null, //having
                    null  //orderBy
            );
            */
            //endregion

            if(cursor.moveToFirst()){
                do {
                    Layer layer = new Layer();

                    SetLayerProperties(layer, cursor);

                    layers.add(layer);

                }while(cursor.moveToNext());
            }

            mEntities = layers;
        }
    };

    protected DbAction UpdateLayer = new DbAction(){
        @Override
        public void Run(){
            ContentValues layerValues = SetLayerContentValues(mEntity);

            mDatabase.update(GeoSQLiteHelper.LAYER_TABLE, layerValues, WhereId(mEntityId), null);
        }
    };

    protected DbAction CreateLayer = new DbAction(){
        @Override
        public void Run(){
            ContentValues layerValues = SetLayerContentValues(mEntity);

            InsertLayer(layerValues);
        }
    };

    protected DbAction CreateMultipleLayers = new DbAction(){
        @Override
        public void Run(){
            for(Layer layer : mEntities){
                ContentValues layerValues = SetLayerContentValues(layer);

                InsertLayer(layerValues);
            }
        }
    };
    //endregion

    //region Helpers
    private void InsertLayer(ContentValues layerValues) {
        mEntityId = (int)mDatabase.insert(GeoSQLiteHelper.LAYER_TABLE, null, layerValues);
    }

    private void SetLayerProperties(Layer layer, Cursor cursor) {

        layer.setName(getStringFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_NAME));
        layer.setId(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_LAYERID));
        layer.setMobileId(getIntFromColumnName(cursor, BaseColumns._ID));
        layer.setGeometryTypeCodeId(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID));
        layer.setIsFixed(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_IS_FIXED));
        layer.setIsOwner(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_IS_OWNER));
        layer.setIsShowing(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_IS_SHOWING));
        layer.setStylePath(getStringFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_STYLEPATH));
        layer.setExtent(getExtentFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_EXTENT));

    }

    protected ContentValues SetLayerContentValues(Layer layer) {
        ContentValues layerValues = new ContentValues();

        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_NAME, layer.getName());
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_LAYERID, layer.getId());
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID, layer.getGeometryTypeCodeId());
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_STYLEPATH, layer.getStylePath());
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_FIXED, ConvertBooleanToInt(layer.getIsFixed()));
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_OWNER, ConvertBooleanToInt(layer.getIsOwner()));
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_SHOWING, ConvertBooleanToInt(layer.getIsShowing()));
        layerValues.put(GeoSQLiteHelper.COLUMN_LAYER_EXTENT, ConvertExtentToJSONString(layer.getExtent()));

        return layerValues;
    }
    //endregion
}
