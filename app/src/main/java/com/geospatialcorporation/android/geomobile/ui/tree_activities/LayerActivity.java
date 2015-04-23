package com.geospatialcorporation.android.geomobile.ui.tree_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LayerActivity extends Activity {

    private Folder mFolder;
    private DataHelper mHelper;
    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.inject(this);

        mHelper = new DataHelper();

        Intent intent = getIntent();

        mFolder = intent.getParcelableExtra(Folder.FOLDER_INTENT);

        setTitle(mFolder.getName());

        //show subfolders & layers in a recycler view
        List<Folder> folders = mFolder.getFolders() == null ? new ArrayList<Folder>() : mFolder.getFolders();

        List<ListItem> listItems = mHelper.CombineLayerItems(mFolder.getLayers(), folders);

        ListItemAdapter listItemAdapter = new ListItemAdapter(this, listItems, ListItemAdapter.LAYER);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Folders"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(folders.size(), "Layers"));

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this,  R.layout.section, R.id.section_text, listItemAdapter);

        mSectionedAdapter.setSections(sections.toArray(dummy));

        mRecyclerView.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
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
