package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import java.util.List;

import retrofit.RetrofitError;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private static final String TAG = "ClientAdapter";
    private Context mContext;
    private List<Client> mClients;
    private int newClientId;
    private LoginService mService;

    public ClientAdapter(Context context, List<Client> clients){
        mContext = context;
        mClients = clients;
        mService = application.getRestAdapter().create(LoginService.class);
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_client, parent, false);

        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        holder.bindClientItem(mClients.get(position));
    }

    @Override
    public int getItemCount() {
        if (mClients != null) {
            return mClients.size();
        } else {
            return 0;
        }
    }

    protected class ClientViewHolder extends RecyclerView.ViewHolder {
        //region Properties
        TextView mClientName;
        Client mClient;
        //endregion

        public ClientViewHolder(View itemView){
            super(itemView);
            mClientName = (TextView)itemView.findViewById(R.id.clientNameLabel);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    newClientId = mClient.Id;

                    new SwitchClientTask().execute();

                    application.setGeoClient(mClient);

                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                }
            });
        }

        public void bindClientItem(Client client){
            mClient = client;
            mClientName.setText(client.Name);
        }

    }



    private class SwitchClientTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                mService.setClient(newClientId);

            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return null;
        }
    }



}