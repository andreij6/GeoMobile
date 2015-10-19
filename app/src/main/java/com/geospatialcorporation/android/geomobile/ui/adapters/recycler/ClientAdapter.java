package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.SubscriptionSelectorActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;

import java.util.List;

import butterknife.Bind;
import retrofit.RetrofitError;

public class ClientAdapter extends GeoRecyclerAdapterBase<ClientAdapter.Holder, Subscription> {
    private static final String TAG = ClientAdapter.class.getSimpleName();

    private Subscription mSelectedSubscription;
    private LoginService mService;

    public ClientAdapter(Context context, List<Subscription> subscriptions) {
        super(context, subscriptions, R.layout.recycler_list_subscription, Holder.class);
        mService = application.getRestAdapter().create(LoginService.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_subscription, parent, false);

        return new Holder(view);
    }

    protected class Holder extends GeoHolderBase<Subscription> {
        //region Properties
        @Bind(R.id.clientNameLabel) TextView mClientName;
        @Bind(R.id.clientTypeLabel) TextView mClientType;
        ClientTypeCodes mClientTypeCodes;
        Subscription mSubscription;
        //endregion

        public Holder(View itemView) {
            super(itemView);

            mClientTypeCodes = new ClientTypeCodes();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedSubscription = mSubscription;
                    new SwitchClientTask().execute();
                }
            });
        }

        public void bind(Subscription subscription) {
            mSubscription = subscription;
            mClientName.setText(subscription.getName());
            mClientType.setText(mClientTypeCodes.getClientTypeName(subscription.getType()));
        }

    }

    private class SwitchClientTask extends AsyncTask<Object,Object,Object> {
        @Override
        protected Object doInBackground(Object[] params) {
            try {

                mService.setClient(mSelectedSubscription.getId());
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object nothing) {
            application.setGeoSubscription(mSelectedSubscription);

            if(DeviceTypeUtil.isTablet(mContext.getResources())) {
                mContext.startActivity(new Intent(mContext, MainTabletActivity.class));
            } else {
                mContext.startActivity(new Intent(mContext, MainActivity.class));
            }
            ((SubscriptionSelectorActivity)mContext).finish();

        }
    }
}