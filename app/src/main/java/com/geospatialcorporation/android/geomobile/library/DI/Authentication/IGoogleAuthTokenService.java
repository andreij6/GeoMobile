package com.geospatialcorporation.android.geomobile.library.DI.Authentication;

import com.geospatialcorporation.android.geomobile.library.DI.Authentication.models.AuthTokenParams;

public interface IGoogleAuthTokenService {
    void GetAndUseAuthToken(AuthTokenParams parameters);
}
