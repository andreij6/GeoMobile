package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.map.GeoCallback;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class LayerStyleTask implements ILayerStyleTask {

    private static final String TAG = LayerStyleTask.class.getSimpleName();

    LegendLayer mLegendLayer;
    Context mContext;
    IGeoAnalytics mAnalytics;
    GeoCallback mCallBack;
    Set<Target> mTargets;

    public LayerStyleTask(){
        mContext = application.getAppContext();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mTargets = new HashSet<>();
    }

    private final static float LDPI = 0.75f;
    private final static float MDPI = 1.0f;
    private final static float HDPI = 1.5f;
    private final static float XHDPI = 2.0f;
    private final static float XXHDPI = 3.0f;
    private final static float XXXHDPI = 4.0f;

    @Override
    public void getStyle(LegendLayer layer, final GeoCallback callback) {
        mLegendLayer = layer;
        mCallBack = callback;

        try {

            if(!mLegendLayer.getIsActiveBitmapLoaded()){
                getImagePicasso(styleTarget);
            } else {
                mCallBack.invokeCallback();
            }

        } catch (Exception e) {
            Log.e(TAG, "Excpetion Message: " + e.getMessage());

            mAnalytics.sendException(e);

        }

    }

    @Override
    public void getActiveStyle(LegendLayer llayer) {
        mLegendLayer = llayer;

        try {
            if(mLegendLayer.getLayer() == null){
                List<LegendLayer> llayers = application.getLegendLayerQueue();

                llayers.remove(llayer);
                return;
            }

            getImagePicasso(activeStyleTarget);



        } catch (Exception e){
            Log.e(TAG, "Excpetion Message: " + e.getMessage());

            mAnalytics.sendException(e);
        }
    }

    private void getImagePicasso(final Target target) {
        final String url = application.getAzureDomain() + mLegendLayer.getLayer().getStylePath() + "/tree.png";

        mTargets.add(target);

        final Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = getTargetWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;

            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        Picasso.with(application.getAppContext())
                .load(url)
                .transform(transformation)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(application.getAppContext())
                                .load(url)
                                .transform(transformation)
                                .into(target);
                    }

                    @Override
                    public void onError() {
                    }
                });



    }

    private int getTargetWidth() {

        float density = mContext.getResources().getDisplayMetrics().density;
        int pixelValue;

        if(density <= LDPI) {
            pixelValue = 12;
        }else  if(density > LDPI && density <= MDPI){
            pixelValue = 16;
        } else if(density > MDPI && density <= HDPI) {
            pixelValue = 25;
        } else if(density > HDPI && density <= XHDPI){
            pixelValue = 33;
        } else if(density > XHDPI && density <= XXHDPI){
            pixelValue = 50; //equivalent to 16.67dp
        } else {
            pixelValue = 66;
        }

        return pixelValue;
    }

    private Target activeStyleTarget = new Target() {
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
                e.printStackTrace();
                mAnalytics.sendException(e);

            }catch (Exception e){
                e.printStackTrace();
                mAnalytics.sendException(e);

            } finally {
                mTargets.remove(activeStyleTarget);
                mTargets = null;
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG, "Error Loading " + mLegendLayer.getLayer().getName());

            mTargets.remove(activeStyleTarget);
            mTargets = null;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG, "On PrepareLoad " + mLegendLayer.getLayer().getName());
        }
    };

    private Target styleTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            try {
                Drawable drawImage = new BitmapDrawable(mContext.getResources(), bitmap);

                mLegendLayer.setLegendIcon(drawImage);

                Bitmap iconBitmap = ((BitmapDrawable) drawImage).getBitmap();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(iconBitmap);

                mLegendLayer.setBitmap(icon);
                mLegendLayer.setImageSrc(mContext);

                mLegendLayer.setIsActiveBitmapLoaded(true);

                mCallBack.invokeCallback();
            } catch (NullPointerException e) {
                Log.d(TAG, "null pointer");
                e.printStackTrace();

                mAnalytics.sendException(e);
            } catch (Exception e) {
                Log.d(TAG, "general exception");
                e.printStackTrace();

                mAnalytics.sendException(e);
            } finally {
                mTargets.remove(styleTarget);
                mTargets = null;

            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG, "Bitmap Failed");

            mTargets.remove(styleTarget);
            mTargets = null;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
