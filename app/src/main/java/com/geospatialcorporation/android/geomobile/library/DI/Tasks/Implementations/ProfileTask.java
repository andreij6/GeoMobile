package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetProfileTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.ProfileTaskParams;
import com.geospatialcorporation.android.geomobile.library.rest.AccountService;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.UserAccount;

import retrofit.RetrofitError;

public class ProfileTask implements IGetProfileTask {
    protected static final String TAG = ProfileTask.class.getSimpleName();

    AccountService mService;

    @Override
    public void run(ProfileTaskParams params) {
        mService = application.getRestAdapter().create(AccountService.class);

        new GetProfileTask(params).execute();

    }

    protected class GetProfileTask extends GeoAsyncTask<Void, Void, UserAccount> {

        public GetProfileTask(ProfileTaskParams params){
            super(params.getExecuter());
        }

        @Override
        protected UserAccount doInBackground(Void... params) {
            UserAccount mUserAccount = new UserAccount();
            try {
                mUserAccount = mService.getProfile();
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + "");
                }
            }

            return mUserAccount;
        }
    }
}
