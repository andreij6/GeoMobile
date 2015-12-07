package com.geospatialcorporation.android.geomobile.library.test;

import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DataHelperTest {

    private static DataHelper mSUT;

   @BeforeClass
    public static void setup(){
       mSUT = new DataHelper();
   }

    //region trimString
    @Test
    public void test_TrimStringProperlyTen(){
        String result = mSUT.trimString("thisstringhasmorecharacters", 10);

        assertEquals("String did not Trim to expected result.", "thisstr...", result);
    }

    @Test
    public void test_TrimStringProperlyTwelve(){
        String result = mSUT.trimString("thisstringhasmorecharacters", 12);

        assertEquals("String did not Trim to expected result.", "thisstrin...", result);
    }
    //endregion


    @Test
    public void test_GetAllDocumentsFromATreeOfFolders(){
       Folder root = setupDocFolderTree();

        ArrayList<Document> result = mSUT.getDocumentsRecursively(root);

        assertEquals(111, result.size());
    }

    //test_GetAllDocumentsFromATreeOfFolders test data setup
    private Folder setupDocFolderTree(){
        Folder result = new Folder();
        result.getDocuments().add(new Document());

        for (int i = 0; i < 10; i++) {
            Folder child = new Folder();
            child.setName("child " + i);
            child.getDocuments().add(new Document());
            result.getFolders().add(child);
        }

        for (Folder child : result.getFolders()) {
            for (int i = 0; i < 10; i++) {
                Folder grandchild = new Folder();
                grandchild.setName("grand child " + i);
                grandchild.getDocuments().add(new Document());
                child.getFolders().add(grandchild);
            }
        }

        return result;
    }

    @AfterClass
    public static void tearDown(){
        mSUT = null;
    }
}

