package com.geospatialcorporation.android.geomobile.models.Interfaces;

import android.os.Bundle;

/**
 * Created by andre on 6/3/2015.
 */
public interface ITreeEntity extends INamedEntity, IdModel {
    Bundle toBundle();
}
