package com.geospatialcorporation.android.geomobile.ui.adapters.cursor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class SubscriptionCursorAdapter extends CursorRecyclerViewAdapter<SubscriptionCursorAdapter.ViewHolder> {

    private LoginService mService;
    private Context mContext;

    private static final String TAG = SubscriptionCursorAdapter.class.getSimpleName();

    public SubscriptionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        mService = application.getRestAdapter().create(LoginService.class);
    }

    @Override
    public void onBindViewHolder(SubscriptionCursorAdapter.ViewHolder viewHolder, Cursor cursor) {
        Subscription subscription = Subscription.fromCursor(cursor);

        viewHolder.mClientName.setText(subscription.getName());
        viewHolder.mClientType.setText("");
        viewHolder.mSubscription = subscription;
    }

    @Override
    public SubscriptionCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_subscription, parent, false);
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.clientNameLabel) TextView mClientName;
        @Bind(R.id.clientTypeLabel) TextView mClientType;
        Subscription mSubscription;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SwitchClientTask(mSubscription).execute();
                }
            });
        }
    }

    private class SwitchClientTask extends AsyncTask<Object, Object, Object> {

        Subscription mSubscription;

        public SwitchClientTask(Subscription s){
            mSubscription = s;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                mService.setClient(mSubscription.getId());

            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object nothing) {
            Intent intent = new Intent(mContext, MainActivity.class);
            application.setGeoSubscription(mSubscription);

            mContext.startActivity(intent);

            //((SubscriptionSelectorActivity) mContext).finish();

        }
    }
}
