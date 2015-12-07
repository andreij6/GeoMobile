package com.geospatialcorporation.android.geomobile.data.tables;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface FolderColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    String FOLDER_ID = "Layer_Id";

    @DataType(DataType.Type.INTEGER)
    String IS_IMPORT_FOLDER = "is_import_folder";

    @DataType(DataType.Type.TEXT)
    String IS_FIXED = "is_fixed";

    @DataType(DataType.Type.TEXT)
    String PARENT_ID = "parent_id";

    @DataType(DataType.Type.INTEGER)
    String ACCESS_LEVEL = "access_level";

    @DataType(DataType.Type.TEXT)
    String NAME = "name";
}
