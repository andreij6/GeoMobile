package com.geospatialcorporation.android.geomobile.data;

import android.net.Uri;

import com.geospatialcorporation.android.geomobile.data.tables.SubscriptionColumns;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = GeoUndergoundProvider.AUTHORITY, database = GeoUndergroundDB.class)
public final class GeoUndergoundProvider {
    public static final String AUTHORITY = "com.geospatialcorporation.android.geomobile";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String SUBSCRIPTIONS = "subscriptions";
    }

    private static Uri buildUri(String...paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths){
            builder.appendPath(path);
        }

        return builder.build();
    }

    @TableEndpoint(table = GeoUndergroundDB.SUBSCRIPTIONS)
    public static class Subscriptions {

        @ContentUri(
                path = Path.SUBSCRIPTIONS,
                type = "vnd.android.cursor.dir/subscription",
                defaultSort = SubscriptionColumns.NAME + " ASC")
        public static final Uri CONTENT_URI =  buildUri(Path.SUBSCRIPTIONS);

        @InexactContentUri(
                name = "SUBSCRIPTION_ID",
                path = Path.SUBSCRIPTIONS + "/#",
                type = "vnd.android.cursor.item/subscription",
                whereColumn = SubscriptionColumns.SUBSCRIPTION_ID,  //May need to change to ._ID
                pathSegment = 1)
        public static Uri withId(int id) {
            return buildUri(Path.SUBSCRIPTIONS, String.valueOf(id));
        }

    }

}
