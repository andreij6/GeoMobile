package com.geospatialcorporation.android.geomobile.data;

import com.geospatialcorporation.android.geomobile.data.tables.*;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = GeoUndergroundDB.VERSION)
public final class GeoUndergroundDB {

    public static final int VERSION = 1;

    @Table(SubscriptionColumns.class)
    public static final String SUBSCRIPTIONS = "subscriptions";

}
