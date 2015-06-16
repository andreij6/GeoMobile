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
        mTableName = GeoSQLiteHelper.LAYER_TABLE;
        mEntityIdColumn = GeoSQLiteHelper.COLUMN_LAYER_LAYERID;
    }

    @Override
    protected void setEntityProperties(Layer layer, Cursor cursor) {
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

    @Override
    protected ContentValues setEntityContentValues(Layer layer) {
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

    @Override
    protected Layer createSetEntityProperties(Cursor cursor) {
        Layer result = new Layer();

        setEntityProperties(result, cursor);

        return result;
    }
    //endregion


}
