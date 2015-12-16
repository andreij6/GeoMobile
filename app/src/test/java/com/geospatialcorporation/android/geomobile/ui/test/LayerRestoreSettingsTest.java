package com.geospatialcorporation.android.geomobile.ui.test;

import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LayerRestoreSettingsTest {

    static LayerFragment.LayerRestoreSettings mSUT;

    @Before
    public void setup(){
        mSUT = new LayerFragment.LayerRestoreSettings();
    }

    @Test
    public void test_initialValues(){
        assertThat(mSUT.getShouldRestore(), is(false));
    }

    @Test
    public void test_getConditionally(){
        int actual = mSUT.getEntityConditionally(5);

        assertThat(actual, is(5));
    }

    @Test
    public void test_getConditionallyAfterReset(){
        mSUT.reset(10);

        int actual = mSUT.getEntityConditionally(5);

        assertThat(actual, is(10));
    }

    @Test
    public void test_getConditionallyAfterReset_AfterGetConditionallyRanOnce(){
        mSUT.reset(10);

        int actual = mSUT.getEntityConditionally(5);

        int realActual = mSUT.getEntityConditionally(12);

        assertThat(realActual, is(12));
    }

    @After
    public void tearDown(){
        mSUT = null;
    }

}
