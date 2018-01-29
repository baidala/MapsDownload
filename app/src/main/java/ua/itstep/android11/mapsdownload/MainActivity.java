package ua.itstep.android11.mapsdownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private final String ATTR_CODE = "M49code";

    ExpandableListView elvMain;
    ExpandableListAdapter adapter;
    AdapterHelper adptrHelper;
    TextView tvDevMemory;
    TextView tvFree;
    TextView tvSize;
    XMLParseHelper xmlParseHelper;
    RegionModel root;
    private FreespaceFragment freespace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onCreate");

        setContentView(R.layout.activity_main);

        root = new RegionModel();
        root.setRegionName(getResources().getString(R.string.app_name));

        xmlParseHelper = new XMLParseHelper(this);
        try {
            xmlParseHelper.setData(root);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        freespace = new FreespaceFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.freespace_container, freespace, "space")
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.list_container, RecyclerViewFragment.newInstance(root), "world")
                .commit();

        adptrHelper = new AdapterHelper(this);
        adapter = adptrHelper.getAdapter();

    } //onCreate

    @Override
    public void onBackPressed() {
        if (root.getDepth() == 1){
            super.onBackPressed();
        } else {
            onItemSelected(root.getParent());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Log.d(Prefs.TAG, getClass().getSimpleName() + ".onOptionsItemSelected");
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onItemSelected( RegionModel region) {
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onItemSelected");

        root = region;
        RecyclerViewFragment fragment = RecyclerViewFragment.newInstance(root);

        try {
            if(root.getDepth() > 1) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(freespace)
                        .commit();
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.freespace_container, freespace)
                        .show(freespace)
                        .commit();
            }
            getSupportActionBar().setTitle(root.getRegionName());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, fragment)
                .commit();


    }

    @Override
    public void onItemDownloaded(RegionModel region) {
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onItemDownloaded");

        DownloadingStatus status = region.getDownloadingStatus();
        DownloadProgressFragment progress = DownloadProgressFragment.newInstance(region);

        if (status.equals(DownloadingStatus.WAITING) || status.equals(DownloadingStatus.IN_PROGRESS)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.freespace_container, progress)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.freespace_container, progress)
                    .hide(progress)
                    .commit();
        }

    }




} //MainActivity
