package com.geospatialcorporation.android.geomobile.library.DI.Authentication;

import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.AuthTokenRetriever;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AuthenticationModule.class)
public interface AuthenticationComponent {

    AuthTokenRetriever provideAuthRetriever();

    IGoogleAuthTokenService provideGoogleAuthTokenService();
}
