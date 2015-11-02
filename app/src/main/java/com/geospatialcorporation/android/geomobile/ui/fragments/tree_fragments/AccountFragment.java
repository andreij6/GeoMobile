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
import com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor.IUserAccountProcessor;
import com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor.UserAccountProcessor;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IAccountFragment;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountFragment extends GeoViewFragmentBase implements IAccountFragment {

    protected static final String TAG = AccountFragment.class.getSimpleName();

    //region Properties
    IUserAccountProcessor mProcessor;
    //endregion

    //region View Setup
    @Bind(R.id.firstName) TextView FirstName;
    @Bind(R.id.lastName) TextView LastName;
    @Bind(R.id.email) TextView Email;
    @Bind(R.id.emailLabel) TextView EmailLabel;
    @Bind(R.id.cellPhone) TextView CellPhone;
    @Bind(R.id.cellPhoneLabel) TextView CellPhoneLabel;
    @Bind(R.id.officePhone) TextView OfficePhone;
    @Bind(R.id.officePhoneLabel) TextView OfficePhoneLabel;

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

    @SuppressWarnings("unused")
    @OnClick(R.id.refreshIV)
    public void refreshAccountInfo(){
        application.setUserAccount(null);

        mProcessor.GetUserAccountData(getActivity(), this);
    }
    //endregion


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProcessor = setProcessor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setView(inflater, container, R.layout.fragment_account);
        SetTitle(R.string.account_title);

        mProcessor.GetUserAccountData(getActivity(), this);

        return mView;
    }

    @Override
    public void fillAccountData(UserAccount userAccount) {

        if(mProcessor == null){
            mProcessor = setProcessor();
        }

        mProcessor.setValues(FirstName, LastName, Email, CellPhone, OfficePhone, CellPhoneLabel, OfficePhoneLabel, EmailLabel);
    }

    private IUserAccountProcessor setProcessor() {
        return new UserAccountProcessor();
    }
}
