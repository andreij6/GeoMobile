package com.geospatialcorporation.android.geomobile.data.test;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.geospatialcorporation.android.geomobile.data.GeoUndergroundDataContract;
import com.geospatialcorporation.android.geomobile.data.GeoUndergroundProvider2;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import org.junit.Test;

public class TestProvider extends AndroidTestCase {

    private static final String TAG = TestProvider.class.getSimpleName();

    private static MockContentResolver mContentResolver;
    private static Activity mActivity;

    //public TestProvider() {
    //    super(GeoUndergroundProvider2.class, GeoUndergroundDataContract.AUTHORITY);
    //}

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        GeoUndergroundProvider2 cp = new GeoUndergroundProvider2();

        mActivity = new MainActivity();

        cp.attachInfo(mActivity, null);

        mContentResolver = new MockContentResolver();
        mContentResolver.addProvider(GeoUndergroundDataContract.AUTHORITY, cp);
    }

    //region Get Type'
    @Test
    public void test_GetType_Folders(){

        //String mimeType = mContext.getContentResolver().getType(GeoUndergroundDataContract.Folders.getContentUri());

        //String mimeType = mContentResolver.getType(GeoUndergroundDataContract.Folders.getContentUri());

        //assertEquals(GeoUndergroundDataContract.Folders.CONTENT_TYPE, mimeType);
    }

    /*
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
    */
}
