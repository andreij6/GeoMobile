package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.LoginUIModel;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.UserLoginModel;

public interface IUserLoginTask {
    void Login(UserLoginModel loginModel, LoginUIModel uiModel);
}
