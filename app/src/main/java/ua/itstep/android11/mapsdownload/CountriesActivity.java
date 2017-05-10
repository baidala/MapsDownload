package ua.itstep.android11.mapsdownload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;


public class CountriesActivity extends AppCompatActivity {
    private final static String TAG = "MAPS >>>>>>>>";

    private final String ATTR_CODE = "M49code";

    private RecyclerViewAdapter lvAdapter;
    private RecyclerView lvCountries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        Intent intent = getIntent();
        String code = intent.getStringExtra(ATTR_CODE);
        Log.d(TAG, getClass().getSimpleName() + ".onCreate  code = " + code);

        lvCountries = (RecyclerView) findViewById(R.id.listCountries);
        lvAdapter = new RecyclerViewAdapter(this, code);
        lvCountries.setAdapter(lvAdapter);

        /*
        Locale locale = new Locale("", "276");
        Log.d(TAG, getClass().getSimpleName() + ".CCCCCcountry = " + locale.getDisplayCountry());
        locale = new Locale("", "155");
        Log.d(TAG, getClass().getSimpleName() + ".CCCCCcountry = " + locale.getCountry());
        */
    }
}
