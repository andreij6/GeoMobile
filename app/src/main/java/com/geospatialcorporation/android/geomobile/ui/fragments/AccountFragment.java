package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.rest.AccountService;
import com.geospatialcorporation.android.geomobile.models.UserAccount;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class AccountFragment extends Fragment {

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    View mRootView;
    AccountService mService;
    //endregion

    //region View Setup
    @InjectView(R.id.firstName) TextView FirstName;
    @InjectView(R.id.lastName) TextView LastName;
    @InjectView(R.id.email) TextView Email;
    @InjectView(R.id.cellPhone) TextView CellPhone;
    @InjectView(R.id.officePhone) TextView OfficePhone;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRootView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, mRootView);

        mService = application.getRestAdapter().create(AccountService.class);
        new GetProfileTask().execute();

        return mRootView;
    }

    private void SetupUI(UserAccount mUserAccount) {
        FirstName.setText(mUserAccount.getFirstName());
        LastName.setText(mUserAccount.getLastName());
        Email.setText(mUserAccount.getEmail());
        CellPhone.setText(mUserAccount.getCellPhone());
        OfficePhone.setText(mUserAccount.getOfficePhone());
    }

    private class GetProfileTask extends AsyncTask<Void, Void, UserAccount>{
        ProgressDialogHelper mProgressHelper;

        public GetProfileTask(){
            mProgressHelper = new ProgressDialogHelper(getActivity());
        }

        @Override
        protected void onPreExecute(){
            mProgressHelper.toggleProgressDialog();
        }

        @Override
        protected UserAccount doInBackground(Void... params) {
            UserAccount mUserAccount = new UserAccount();
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
            SetupUI(account);
            mProgressHelper.toggleProgressDialog();
        }
    }

}
