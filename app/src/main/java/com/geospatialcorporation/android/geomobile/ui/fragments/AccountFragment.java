package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.AccountService;
import com.geospatialcorporation.android.geomobile.models.UserAccount;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;

public class AccountFragment extends Fragment {

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    View mRootView;
    UserAccount mUserAccount;
    AccountService mService;
    //endregion

    //region View Setup
    @InjectView(R.id.firstNameET) EditText FirstName;
    @InjectView(R.id.lastNameEt) EditText LastName;
    @InjectView(R.id.emailET) EditText Email;
    @InjectView(R.id.cellPhoneET) EditText CellPhone;
    @InjectView(R.id.officePhoneET) EditText OfficePhone;
    //endregion

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRootView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, mRootView);

        mService = application.getRestAdapter().create(AccountService.class);

        new GetProfileTask().execute();

        return mRootView;
    }

    private void SetupUI() {
        FirstName.setText(mUserAccount.getFirstName());
        LastName.setText(mUserAccount.getLastName());
        Email.setText(mUserAccount.getEmail());
        CellPhone.setText(mUserAccount.getCellPhone());
        OfficePhone.setText(mUserAccount.getOfficePhone());
    }

    @OnClick(R.id.saveAcctBtn)
    public void save(){
        Toast.makeText(getActivity(), "Your Account changes have NOT Been saved!!", Toast.LENGTH_LONG).show();
    }

    private class GetProfileTask extends AsyncTask<Void, Void, UserAccount>{

        @Override
        protected UserAccount doInBackground(Void... params) {
            try {
                mUserAccount = mService.getProfile();
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 70");
                }
            }

            return mUserAccount;
        }

        @Override
        protected void onPostExecute(UserAccount account){
            SetupUI();
        }
    }

}
