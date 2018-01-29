package ua.itstep.android11.mapsdownload;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class CountriesActivity extends AppCompatActivity {

    private final String ATTR_CODE = "M49code";
    private static Bundle bundleState;

    RecyclerViewAdapter lvAdapter;
    RecyclerView lvCountries;
    TextView tvDownload;
    TextView tvCountry;
    TextView tvProgress;
    ProgressBar progressBar;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        Intent intent = getIntent();

        String code = intent.getStringExtra(ATTR_CODE);
        if ( code == null) code = Prefs.ERROR_CODE;
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onCreate  code = " + code);

        lvCountries = (RecyclerView) findViewById(R.id.listCountries);
        //lvAdapter = new RecyclerViewAdapter(this, code);
        //lvCountries.setAdapter(lvAdapter);

        tvDownload = (TextView) findViewById(R.id.tvDownload);
        tvDownload.setText(R.string.download);

        tvCountry = (TextView) findViewById(R.id.regionName);
        tvCountry.setText(R.string.country);

        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvProgress.setText( R.string.progress );

        progressBar = (ProgressBar)findViewById(R.id.pBar);

        frameLayout = (FrameLayout)findViewById(R.id.frame);
        /*
        Locale locale = new Locale("", "276");
        Log.d(TAG, getClass().getSimpleName() + ".CCCCCcountry = " + locale.getDisplayCountry());
        locale = new Locale("", "155");
        Log.d(TAG, getClass().getSimpleName() + ".CCCCCcountry = " + locale.getCountry());
        */
        if (savedInstanceState != null){
            restorePreviousState(savedInstanceState);
        }

        registerReceiver();

    }

    private void restorePreviousState(Bundle savedInstanceState){
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".restorePreviousState " );
        Parcelable listState = savedInstanceState.getParcelable(Prefs.KEY_RECYCLER_STATE);
        ArrayList<RegionModel> dataSet = savedInstanceState.getParcelableArrayList(Prefs.KEY_RECYCLER_DATASET_STATE);
        lvAdapter.setItems(dataSet);
        lvCountries.getLayoutManager().onRestoreInstanceState(listState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onPause " );
        bundleState = new Bundle();
        Parcelable listState = lvCountries.getLayoutManager().onSaveInstanceState();
        bundleState.putParcelable(Prefs.KEY_RECYCLER_STATE, listState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onResume " );
        if(bundleState != null){
            Parcelable listState = bundleState.getParcelable(Prefs.KEY_RECYCLER_STATE);
            lvCountries.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onSaveInstanceState");
        Parcelable listState = lvCountries.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Prefs.KEY_RECYCLER_STATE, listState);
        outState.putParcelableArrayList(Prefs.KEY_RECYCLER_DATASET_STATE, lvAdapter.getParcelableList());
        super.onSaveInstanceState(outState);
    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Prefs.MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO check m49code
            if(intent.getAction().equals(Prefs.MESSAGE_PROGRESS)){

                RegionModel download = intent.getParcelableExtra("download");
                progressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){
                    //mProgressText.setText("File Download Complete");
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                    progressBar.setIndeterminate(false);

                } else {
                    //mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));
                    tvProgress.setText( String.format(Locale.ENGLISH, "%d %",download.getProgress()) );
                    progressBar.setProgress(download.getProgress());
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Prefs.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Prefs.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startDownload();
                } else {
                    //Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }





}
