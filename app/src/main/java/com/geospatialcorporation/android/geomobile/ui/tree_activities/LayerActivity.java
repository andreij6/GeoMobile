package com.geospatialcorporation.android.geomobile.ui.tree_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LayerActivity extends Activity {

    private Folder mFolder;
    private DataHelper mHelper;
    Folder mParent;
    @InjectView(R.id.libraryitem_recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.inject(this);

        mHelper = new DataHelper();

        Intent intent = getIntent();

        mFolder = intent.getParcelableExtra(Folder.FOLDER_INTENT);

        setTitle(mFolder.getName());

        SectionTreeBuilder builder = new SectionTreeBuilder(this)
                .AddLayerData(mFolder.getLayers(), mFolder.getFolders(), mParent)
                .BuildAdapter(ListItemAdapter.LAYER)
                .setRecycler(mRecyclerView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
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
}
