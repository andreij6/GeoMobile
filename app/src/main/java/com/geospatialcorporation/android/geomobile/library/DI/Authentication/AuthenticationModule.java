package com.geospatialcorporation.android.geomobile.library.DI.Authentication;

import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.AuthTokenRetriever;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.GoogleAuthTokenService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthenticationModule {

    @Provides @Singleton
    AuthTokenRetriever provideAuthRetriever(){ return new AuthTokenRetriever();}

    @Provides @Singleton
    IGoogleAuthTokenService provideGoogleAuthTokenService(){return new GoogleAuthTokenService(new AuthTokenRetriever());}
}
