package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Client;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private static final String TAG = "ClientAdapter";
    private Context mContext;
    private List<Client> mClients;

    public ClientAdapter(Context context, List<Client> clients){
        mContext = context;
        mClients = clients;
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
        //endregion

        public ClientViewHolder(View itemView){
            super(itemView);
            mClientName = (TextView)itemView.findViewById(R.id.clientNameLabel);
        }

        public void bindClientItem(Client client){
            mClientName.setText(client.Name);
        }

        protected View.OnClickListener ClientItemClicked = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
              //
            }
        };
    }
}