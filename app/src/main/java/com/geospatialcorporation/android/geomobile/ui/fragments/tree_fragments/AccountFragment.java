package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetProfileTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.ProfileTaskParams;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountFragment extends GeoViewFragmentBase implements IPostExecuter<UserAccount>{

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    IGetProfileTask mProfileTask;
    ProgressDialogHelper mProgressDialogHelper;
    //endregion

    //region View Setup
    @InjectView(R.id.firstName) TextView FirstName;
    @InjectView(R.id.lastName) TextView LastName;
    @InjectView(R.id.email) TextView Email;
    @InjectView(R.id.cellPhone) TextView CellPhone;
    @InjectView(R.id.officePhone) TextView OfficePhone;

    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.showNavIV2)
    public void showNavigation2(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setView(inflater, container, R.layout.fragment_account);
        SetTitle(R.string.account_title);

        mProfileTask = application.getTasksComponent().provideProfileTask();
        mProgressDialogHelper = new ProgressDialogHelper(getActivity());

        mProgressDialogHelper.showProgressDialog();
        mProfileTask.run(new ProfileTaskParams(this));

        mNavigationHelper.syncMenu(3);

        return mView;
    }

    @Override
    public void onPostExecute(UserAccount model) {
        mProgressDialogHelper.hideProgressDialog();
        FirstName.setText(model.getFirstName());
        LastName.setText(model.getLastName());
        Email.setText(model.getEmail());
        CellPhone.setText(model.getCellPhone());
        OfficePhone.setText(model.getOfficePhone());
    }



}
