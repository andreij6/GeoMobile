package com.geospatialcorporation.android.geomobile.ui.tree_activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class DocumentsActivity extends Activity {

    protected static final String TAG = DocumentsActivity.class.getSimpleName();

    //region Properties
    protected Folder mFolder;
    private FolderService mService;
    private List<Document> mDocumentList;
    Context mContext;
    DataHelper mHelper;
    //endregion

    @InjectView(R.id.documentlist) RecyclerView mRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        ButterKnife.inject(this);
        mContext = this;
        mHelper = new DataHelper();

        mService = application.getRestAdapter().create(FolderService.class);

        Intent intent = getIntent();

        mFolder = intent.getParcelableExtra(Folder.FOLDER_INTENT);

        setTitle(mFolder.getName());

        new GetDocumentsTask().execute();

        mRecyclerView.setHasFixedSize(true);

        new GetDocumentsTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_folder_documents, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            try {
                mDocumentList = mService.getFolderDocuments(mFolder.getId());
                application.setDocuments(mDocumentList);
            } catch (RetrofitError e){
                Log.d(TAG, e.getMessage());
            }
            return mDocumentList;
        }

        @Override
        protected void onPostExecute(List<Document> documents){

            SectionTreeBuilder builder = new SectionTreeBuilder(mContext)
                                        .AddLibraryData(mDocumentList, mFolder.getFolders())
                                        .BuildAdapter(ListItemAdapter.LIBRARY)
                                        .setRecycler(mRecyclerView);
        }
    }


}
