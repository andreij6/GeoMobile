package com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetProfileTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.ProfileTaskParams;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IAccountFragment;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class UserAccountProcessor implements IUserAccountProcessor, IPostExecuter<UserAccount> {

    UserAccount mUserAccount;
    IGetProfileTask mProfileTask;
    ProgressDialogHelper mProgressDialogHelper;
    IAccountFragment mExecuter;

    //region IUserAccountProcessor Contract
    @Override
    public IUserAccountProcessor GetUserAccountData(Context context, IAccountFragment accountFragment) {
        if(AccountInfoStored()) {
            accountFragment.fillAccountData(getUserAccount());
        } else {
            getUserAccountAsync(context, accountFragment);
        }

        return this;
    }

    @Override
    public void setValues(TextView firstName, TextView lastName, TextView email, TextView cellPhone, TextView officePhone, TextView cellPhoneLabel, TextView officePhoneLabel, TextView emailLabel) {
        if(mUserAccount == null){
            mUserAccount = application.getUserAccount();
        }

        setValue(firstName, mUserAccount.getFirstName());
        setValue(lastName, mUserAccount.getLastName());
        setValue(email, mUserAccount.getEmail(), "No Email Listed");
        setValue(cellPhone, mUserAccount.getFormattedCellPhone(), "(---) --- ----");
        setValue(officePhone, mUserAccount.getFormattedOfficePhone(), "(---) --- ----");
    }
    //endregion

    //region IPostExecuter Contract
    @Override
    public void onPostExecute(UserAccount model) {
        mProgressDialogHelper.hideProgressDialog();
        application.setUserAccount(model);
        mUserAccount = model;
        fillAccountData(model);
    }
    //endregion

    //region Helpers
    protected boolean AccountInfoStored() {
        mUserAccount = application.getUserAccount();

        return mUserAccount != null;
    }

    protected void setValue(TextView textView, String value) {
        textView.setText(value);
    }

    protected void setValue(TextView textView, String value, String defaultValue) {
        if(value == null || value == ""){
            textView.setText(defaultValue);
        } else {
            textView.setText(value);
        }
    }

    protected UserAccount getUserAccount() {
        return mUserAccount;
    }

    protected void getUserAccountAsync(Context context, IAccountFragment executer) {
        mExecuter = executer;

        mProfileTask = application.getTasksComponent().provideProfileTask();
        mProgressDialogHelper = new ProgressDialogHelper(context);

        mProgressDialogHelper.showProgressDialog();
        mProfileTask.run(new ProfileTaskParams(this));
    }

    protected void fillAccountData(UserAccount model){
        mExecuter.fillAccountData(model);
    }
    //endregion
}
