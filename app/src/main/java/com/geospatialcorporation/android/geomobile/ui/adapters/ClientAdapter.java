package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoRecyclerAdapterBase;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class ClientAdapter extends GeoRecyclerAdapterBase<ClientAdapter.Holder, Client> {
    private static final String TAG = ClientAdapter.class.getSimpleName();

    private Client mSelectedClient;
    private LoginService mService;

    public ClientAdapter(Context context, List<Client> clients) {
        super(context, clients, R.layout.recycler_list_client, Holder.class);
        mService = application.getRestAdapter().create(LoginService.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_client, parent, false);

        return new Holder(view);
    }

    protected class Holder extends GeoHolderBase<Client> {
        //region Properties
        @InjectView(R.id.clientNameLabel) TextView mClientName;
        @InjectView(R.id.impersonate) Button mImpersonateBtn;
        @InjectView(R.id.clientTypeLabel) TextView mClientType;
        ClientTypeCodes mClientTypeCodes;
        Client mClient;
        //endregion

        public Holder(View itemView) {
            super(itemView);

            mClientTypeCodes = new ClientTypeCodes();

            mImpersonateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedClient = mClient;
                    new SwitchClientTask().execute();
                }
            });
        }

        public void bind(Client client) {
            mClient = client;
            mClientName.setText(client.getName());
            mClientType.setText(mClientTypeCodes.getClientTypeName(client.getType()));
        }

    }

    private class SwitchClientTask extends AsyncTask<Object,Object,Object> {
        @Override
        protected Object doInBackground(Object[] params) {
            try {

                mService.setClient(mSelectedClient.getId());
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object nothing) {
            application.setGeoClient(mSelectedClient);
            mContext.startActivity(new Intent(mContext, MainActivity.class));
        }
    }
}