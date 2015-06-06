package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Bookmark;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.AppSourceBase;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkAppSource extends AppSourceBase<Bookmark> {

    public BookmarkAppSource() {
        super(application.getBookmarkHashMap());
    }
}
