package com.geospatialcorporation.android.geomobile.library;

import com.geospatialcorporation.android.geomobile.models.Document.Document;

public interface ISendFileCallback {
    void invoke(Document response);
}
