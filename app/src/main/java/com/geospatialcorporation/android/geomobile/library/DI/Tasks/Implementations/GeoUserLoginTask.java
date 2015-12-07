package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.UserLoginModel;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFullExecuter;

public class GeoUserLoginTask implements IUserLoginTask {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    public void Login(UserLoginModel loginModel) {
        new UserLoginTask(loginModel).execute();
    }

    //TODO: use geoAsync
    private class UserLoginTask extends GeoAsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private IFullExecuter mContext;


        UserLoginTask(UserLoginModel loginModel) {
            super(loginModel.getExecuter());
            mEmail = loginModel.getEmail();
            mPassword = loginModel.getPassword();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onCancelled() {
            mContext.onCancelled();
        }
    }
}
