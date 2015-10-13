package com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor;

import android.content.Context;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IAccountFragment;


public interface IUserAccountProcessor {

    IUserAccountProcessor GetUserAccountData(Context context, IAccountFragment accountFragment);

    void setValues(TextView name, TextView lastName, TextView textView, TextView firstName, TextView LastName, TextView email, TextView cellPhone, TextView OfficePhone);
}
