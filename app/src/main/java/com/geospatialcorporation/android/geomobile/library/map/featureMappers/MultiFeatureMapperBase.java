package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/26/2015.
 */
public abstract class MultiFeatureMapperBase<T, H extends SingleFeatureMapperBase> extends FeatureMapperBase<T> {

    List<T> mOptions;
    H mSingleMapper;

    public MultiFeatureMapperBase(H singleMapper){
        mOptions = new ArrayList<>();
        mSingleMapper = singleMapper;
    }

    protected void drawFeature(List<Geometry> geometries){
        int geomCount = geometries.size();

        Geometry[] features = new Geometry[geomCount];
        geometries.toArray(features);

        for (int i = 0; i < geomCount; i++) {

            T option = newOptionType();

            mSingleMapper.drawFeature(features[i], option);
            mOptions.add(option);
        }
    }

    protected abstract T newOptionType();

    @Override
    public IFeatureMapper addStyle(Style style) {

        for (T option : mOptions) {
            mColor = mSingleMapper.addStyles(option, style);
        }

        return this;
    }

    @Override
    public void commit(LegendLayer llayer) {

        for(T option : mOptions){
            mSingleMapper.addMapObject(llayer, option);
        }

        setLegendIcon(llayer);
    }

    @Override
    public void reset() {
        mOptions = new ArrayList<>();
    }

}
