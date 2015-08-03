package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetProfileTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.ProfileTaskParams;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccountFragment extends GeoViewFragmentBase implements IPostExecuter<UserAccount>{

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    IGetProfileTask mProfileTask;
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
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, v);

        SetTitle(R.string.account_title);

        mProfileTask = application.getTasksComponent().provideProfileTask();
        mProfileTask.run(new ProfileTaskParams(this));

        return v;
    }

    @Override
    public void onPostExecute(UserAccount model) {
        FirstName.setText(model.getFirstName());
        LastName.setText(model.getLastName());
        Email.setText(model.getEmail());
        CellPhone.setText(model.getCellPhone());
        OfficePhone.setText(model.getOfficePhone());
    }
}
