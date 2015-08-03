package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.os.AsyncTask;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.LoginUIModel;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.UserLoginModel;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;

public class GeoUserLoginTask implements IUserLoginTask {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    public void Login(UserLoginModel loginModel, LoginUIModel model) {
        new UserLoginTask(loginModel, model).execute();
    }

    //TODO: use geoAsync
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private LoginActivity mContext;
        private EditText mPasswordView;
        private IUserLoginTask mTask;


        UserLoginTask(UserLoginModel loginModel, LoginUIModel uiModel) {
            mEmail = loginModel.getEmail();
            mPassword = loginModel.getPassword();
            mContext = uiModel.getContext();
            mPasswordView = uiModel.getPasswordView();
            mTask = uiModel.getTask();
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
        protected void onPostExecute(final Boolean success) {
            mTask = null;

            mContext.showProgress(false);

            if (success) {
                mContext.finish();
            } else {
                mPasswordView.setError(mContext.getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;

            mContext.showProgress(false);
        }
    }
}
