package com.geospatialcorporation.android.geomobile.data.test;

import android.content.ContentResolver;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.geospatialcorporation.android.geomobile.data.GeoUndergroundProvider2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;


public class TestProvider extends ProviderTestCase2<GeoUndergroundProvider2> {

    private static final String TAG = TestProvider.class.getSimpleName();

    private static MockContentResolver mContentResolver;

    public TestProvider() {
        super(GeoUndergroundProvider2.class, GeoUndergroundProvider2.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContentResolver = this.getMockContentResolver();
    }

    //region Get Type'
    @Test
    public void test_GetType_Folders(){

        String mimeType = mContentResolver.getType(GeoUndergroundProvider2.Folders.CONTENT_URI);

        assertEquals(GeoUndergroundProvider2.Folders.CONTENT_TYPE, mimeType);
    }

    @Test
    public void test_GetType_Layers(){

        String mimeType = mContentResolver.getType(GeoUndergroundProvider2.Layers.CONTENT_URI);

        assertEquals(GeoUndergroundProvider2.Layers.CONTENT_TYPE, mimeType);
    }

    @Test
    public void test_GetType_Documents(){

        String mimeType = mContentResolver.getType(GeoUndergroundProvider2.Documents.CONTENT_URI);

        assertEquals(GeoUndergroundProvider2.Folders.CONTENT_TYPE, mimeType);
    }
    //endregion

    @Test
    public void test_BasicFolderQuery(){
        Cursor folderCursor = mContentResolver.query(GeoUndergroundProvider2.Folders.CONTENT_URI,
                                    null, null, null, null);


    }
}
