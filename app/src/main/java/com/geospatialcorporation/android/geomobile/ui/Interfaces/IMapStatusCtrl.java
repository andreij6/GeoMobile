package com.geospatialcorporation.android.geomobile.ui.Interfaces;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public interface IMapStatusCtrl {
    View getLoadingBar();

    TextView getStatusBarMessage();

    ImageView getLoadingAnimIV();

    ImageView getFinishLoadingAnimIV();
}
