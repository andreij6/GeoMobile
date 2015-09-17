package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.map.GeoCallback;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LayerStyleTask implements ILayerStyleTask {

    private static final String TAG = LayerStyleTask.class.getSimpleName();

    LegendLayer mLegendLayer;
    Context mContext;

    public LayerStyleTask(){
        mContext = application.getMainActivity();
    }

    @Override
    public void getStyle(LegendLayer layer, final GeoCallback callback) {
        mLegendLayer = layer;

        try {
            String url = "https://geoeastusfilesdev01.blob.core.windows.net/icons/" + mLegendLayer.getLayer().getStylePath() + "/tree.png";

            Picasso.with(application.getMainActivity())
                    .load(url)
                    .resize(50, 50)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            try {
                                Drawable drawImage = new BitmapDrawable(mContext.getResources(), bitmap);

                                mLegendLayer.setLegendIcon(drawImage);

                                mLegendLayer.setImageSrc(mContext);

                                Bitmap iconBitmap = ((BitmapDrawable) drawImage).getBitmap();
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(iconBitmap);

                                mLegendLayer.setBitmap(icon);

                                callback.invokeCallback();
                            } catch (Exception e){
                                Log.d(TAG, e.getMessage());
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d(TAG, "Bitmap Failed");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

            //mLegendLayer.getCheckBox().setEnabled(true);
        } catch (Exception e){
            Log.e(TAG, "Excpetion Message: " + e.getMessage());
        }
    }
}
