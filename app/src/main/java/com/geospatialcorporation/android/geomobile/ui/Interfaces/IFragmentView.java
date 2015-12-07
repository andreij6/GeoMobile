package com.geospatialcorporation.android.geomobile.ui.Interfaces;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

public interface IFragmentView {
    void setDetailFrame(View view, FragmentManager fm, Bundle bundle);

    void setContentFragment(FragmentManager fm, Bundle bundle);
}
