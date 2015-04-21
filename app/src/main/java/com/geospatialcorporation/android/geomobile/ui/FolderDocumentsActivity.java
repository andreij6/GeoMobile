package com.geospatialcorporation.android.geomobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

public class FolderDocumentsActivity extends Activity {

    //region Properties & Tag
    protected static final String TAG = FolderDocumentsActivity.class.getSimpleName();

    protected Folder mFolder;
    private FolderService mService;
    private List<Document> mDocumentList;
    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mFolder = intent.getParcelableExtra(Folder.FOLDER_INTENT);

        setTitle(mFolder.getName());

        new GetDocumentsTask().execute();

        setContentView(R.layout.activity_folder_documents);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_folder_documents, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetDocumentsTask extends AsyncTask<Void, Void, List<Document>>{

        @Override
        protected List<Document> doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(List<Document> documents){

        }
    }
}
