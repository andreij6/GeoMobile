package com.geospatialcorporation.android.geomobile.data.tables;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface SubscriptionColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    String SUBSCRIPTION_ID = "subscription_id";

    @DataType(DataType.Type.INTEGER)
    String TYPE = "type";

    @DataType(DataType.Type.TEXT)
    String NAME = "name";

    @DataType(DataType.Type.TEXT)
    String CREATED = "created";

    @DataType(DataType.Type.INTEGER)
    String SSP = "ssp";
}
