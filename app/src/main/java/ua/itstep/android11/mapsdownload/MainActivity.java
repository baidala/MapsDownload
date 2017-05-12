package ua.itstep.android11.mapsdownload;

import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String ATTR_CODE = "M49code";

     ExpandableListView elvMain;
     ExpandableListAdapter adapter;
     AdapterHelper adptrHelper;
     TextView tvDevMemory;
     TextView tvFree;
     TextView tvSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onCreate");

        setContentView(R.layout.activity_main);

        adptrHelper = new AdapterHelper(this);
        adapter = adptrHelper.getAdapter();

        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);

        tvDevMemory = (TextView) findViewById(R.id.tvDevMemory);
        tvDevMemory.setText(R.string.dev_memory);

        tvFree = (TextView) findViewById(R.id.tvFree);
        tvFree.setText(R.string.free);

        tvSize = (TextView) findViewById(R.id.tvSize);
        tvSize.setText( getAvailableInternalMemorySize() );

        elvMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(Prefs.TAG, "onChildClick  groupPosition = "+groupPosition +"  childPosition = "+ childPosition + " code = " + adptrHelper.getChildCode(groupPosition, childPosition));
                Intent intent = new Intent(getApplicationContext(), CountriesActivity.class);
                intent.putExtra( ATTR_CODE, adptrHelper.getChildCode(groupPosition, childPosition) );
                startActivity(intent);

                return false;
            }
        });

    } //onCreate

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String floatForm (double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String formatSize(long size) {

        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";

        return "???";
    }


} //MainActivity
