package com.geospatialcorporation.android.geomobile.ui.test;

import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;

import junit.framework.Assert;

import org.junit.Test;

public class LayerManagerTest {
    @Test
    public void getExtentTest_1(){
        LayerManager sut = new LayerManager();

        Extent only = new Extent(8,new Point(1, 40.28895415740959, -79.37759399414062), new Point(1, 40.07912221750036, -80.47348022460938));

        sut.addVisibleLayerExtent(1, only) ;

        Extent extent = sut.getFullExtent();

        String extentString = extent.toString();
        String onlyString = only.toString();

        Assert.assertEquals(onlyString, extentString);
    }

    @Test
      public void getExtentTest_2(){
        LayerManager sut = new LayerManager();
        //min                                               //max
        Extent first = new Extent(8,new Point(1, 40.28895415740959, -79.37759399414062), new Point(1, 40.07912221750036, -80.47348022460938));
        Extent second = new Extent(8, new Point(1, 36.49666668157884, -90.21166667383856), new Point(1, 33.02500004815358, -94.54416664983293));

        sut.addVisibleLayerExtent(1, first);
        sut.addVisibleLayerExtent(2, second);

        Extent extent = sut.getFullExtent();

        String extentString = extent.toString();

        Extent expected = new Extent(8, new Point(1, 36.49666668157884, -90.21166667383856), new Point(1, 40.07912221750036, -80.47348022460938));

        String expectedString = expected.toString();

        Assert.assertEquals(expectedString, extentString);
    }

}
