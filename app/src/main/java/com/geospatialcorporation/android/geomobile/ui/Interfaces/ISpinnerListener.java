package com.geospatialcorporation.android.geomobile.ui.Interfaces;

import java.util.List;

public interface ISpinnerListener<T> {
    void setSelected(T selected);

    List<T> getData();
}
