package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.accounts.Account;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Client;


import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class AccountFragment extends Fragment {

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    View mRootView;
    Client mClient;
    LoginService mService;
    //endregion

    //region View Setup
    @InjectView(R.id.nameTV) TextView NameTV;
    @InjectView(R.id.emailTV) TextView EmailTV;
    @InjectView(R.id.phoneTV) TextView PhoneTV;
    //endregion

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRootView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, mRootView);

        mService = application.getRestAdapter().create(LoginService.class);

        new GetProfileTask().execute();

        return mRootView;
    }

    private void SetupUI() {
        NameTV.setText(mClient.Name);
    }

    private class GetProfileTask extends AsyncTask<Void, Void, Client>{

        @Override
        protected Client doInBackground(Void... params) {
            try {
                mClient = mService.getCurrentClient();
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 70");
                }
            }

            return mClient;
        }

        @Override
        protected void onPostExecute(Client client){
            SetupUI();
        }
    }

}
