package ua.itstep.android11.mapsdownload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;


public class CountriesActivity extends AppCompatActivity {

    private final String ATTR_CODE = "M49code";

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
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onCreate  code = " + code);

        lvCountries = (RecyclerView) findViewById(R.id.listCountries);
        lvAdapter = new RecyclerViewAdapter(this, code);
        lvCountries.setAdapter(lvAdapter);

        tvDownload = (TextView) findViewById(R.id.tvDownload);
        tvDownload.setText(R.string.download);

        tvCountry = (TextView) findViewById(R.id.tvCountry);
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
    }
}
