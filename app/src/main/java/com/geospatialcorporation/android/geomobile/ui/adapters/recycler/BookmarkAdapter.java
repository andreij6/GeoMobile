package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.BookmarkPosition;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.Bind;

public class BookmarkAdapter extends GeoRecyclerAdapterBase<BookmarkAdapter.Holder, Bookmark> {

    GoogleMap mGoogleMap;

    public BookmarkAdapter(Context context, List<Bookmark> data, GoogleMap map){
        super(context, data, R.layout.recycler_bookmarks, Holder.class);
        mGoogleMap = map;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }


    protected class Holder extends GeoHolderBase<Bookmark> {

        @Bind(R.id.bookmarkName) TextView mBookmarkName;

        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(final Bookmark bookmark) {
            mBookmarkName.setText(bookmark.getName());
            mView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    BookmarkPosition p = bookmark.getPosition();
                    LatLng target = new LatLng(p.getLat(), p.getLng());

                    CameraPosition position = new CameraPosition(target, p.getZoom(), p.getTilt(), p.getBearing());
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                    mGoogleMap.animateCamera(update);

                    Toast.makeText(mContext, "Zooming to " + bookmark.getName(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
