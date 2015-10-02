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

import java.util.List;
import java.util.Queue;

public class LayerStyleTask implements ILayerStyleTask {

    private static final String TAG = LayerStyleTask.class.getSimpleName();

    LegendLayer mLegendLayer;
    Context mContext;

    public LayerStyleTask(){
        mContext = application.getAppContext();
    }

    @Override
    public void getStyle(LegendLayer layer, final GeoCallback callback) {
        mLegendLayer = layer;

        try {
            String url = application.getAzureDomain() + mLegendLayer.getLayer().getStylePath() + "/tree.png";

            Picasso.with(application.getAppContext())
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

                                mLegendLayer.setIsActiveBitmapLoaded(true);

                                callback.invokeCallback();
                            } catch (NullPointerException e) {
                                Log.d(TAG, "null pointer");
                                e.printStackTrace();
                            } catch (Exception e) {
                                Log.d(TAG, "general exception");
                                e.printStackTrace();
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
        } catch (Exception e) {
            Log.e(TAG, "Excpetion Message: " + e.getMessage());
        }

    }

    @Override
    public void getActiveStyle(LegendLayer llayer) {
        mLegendLayer = llayer;

        try {
            if(mLegendLayer.getLayer() == null){
                Log.d(TAG, "POLLED");
                List<LegendLayer> llayers = application.getLegendLayerQueue();

                llayers.remove(llayer);
                return;
            }

            String url = application.getAzureDomain() + mLegendLayer.getLayer().getStylePath() + "/tree.png";

            Picasso.with(application.getAppContext())
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

                                mLegendLayer.setIsActiveBitmapLoaded(true);

                            }catch (NullPointerException e){
                                Log.d(TAG, "null pointer");
                                e.printStackTrace();
                            }catch (Exception e){
                                Log.d(TAG, "general exception");
                                e.printStackTrace();
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
