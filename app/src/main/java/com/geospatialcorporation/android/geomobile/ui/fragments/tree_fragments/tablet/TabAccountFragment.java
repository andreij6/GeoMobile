package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor.IUserAccountProcessor;
import com.geospatialcorporation.android.geomobile.library.services.UserAccountProccessor.UserAccountProcessor;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IAccountFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import butterknife.Bind;

public class TabAccountFragment extends TabGeoViewFragmentBase implements IAccountFragment {

    @Bind(R.id.firstName) TextView FirstName;
    @Bind(R.id.lastName) TextView LastName;
    @Bind(R.id.email) TextView Email;
    @Bind(R.id.cellPhone) TextView CellPhone;
    @Bind(R.id.officePhone) TextView OfficePhone;

    IUserAccountProcessor mProcessor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mProcessor = new UserAccountProcessor().GetUserAccountData(getActivity(), this);

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.account_tab_fragment;
    }

    @Override
    public void fillAccountData(UserAccount userAccount) {
        if(mProcessor == null){
            mProcessor = new UserAccountProcessor();
        }

        mProcessor.setValues(FirstName, LastName, Email, CellPhone, OfficePhone);
    }
}
