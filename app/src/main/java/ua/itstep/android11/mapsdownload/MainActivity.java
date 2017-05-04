package ua.itstep.android11.mapsdownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MAPS >>>>>>>>";
    private ArrayList<String> list = new ArrayList<String>();

    private ArrayList<Map<String, String>> regionsData;
    private ArrayList<Map<String, String>> africaData;
    private ArrayList<Map<String, String>> americasData;
    private ArrayList<Map<String, String>> asiaData;
    private ArrayList<Map<String, String>> europeData;
    private ArrayList<Map<String, String>> oceaniaData;


    private ArrayList<ArrayList<Map<String, String>>> allChildData;
    private Map<String, String> m;
    private ExpandableListView elvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Locale[] locales = Locale.getAvailableLocales();

        //regions UN M49 codes
        Locale locAfrica = new Locale("", "002");
        Locale locAmericas = new Locale("", "019");
        //Locale locAntarctica = new Locale("", "010");
        Locale locAsia = new Locale("", "142");
        Locale locEurope = new Locale("", "150");
        Locale locOceania = new Locale("", "009");

        String[] regions = new String[] {locAfrica.getDisplayName(),
                                        locAmericas.getDisplayName(),
                                        //locAntarctica.getDisplayName(),
                                        locAsia.getDisplayName(),
                                        locEurope.getDisplayName(),
                                        locOceania.getDisplayName() };


        //Africa
        Locale locNorthernAfrica = new Locale("", "015");
        Locale locEasternAfrica = new Locale("", "014");
        Locale locMiddleAfrica = new Locale("", "017");
        Locale locSouthernAfrica = new Locale("", "018");
        Locale locWesternAfrica = new Locale("", "011");
        //Locale locSubSaharanAfrica = new Locale("", "202");

        String[] regionAfrica = new String[] {
                locNorthernAfrica.getDisplayName(),
                locEasternAfrica.getDisplayName(),
                locMiddleAfrica.getDisplayName(),
                locSouthernAfrica.getDisplayName(),
                locWesternAfrica.getDisplayName() };


        //Americas
        Locale locCaribbean = new Locale("", "029");
        Locale locCentralAmerica = new Locale("", "013");
        Locale locSouthAmerica = new Locale("", "005");
        Locale locNorthernAmerica = new Locale("", "021");

        String[] regionAmericas = new String[] {
                locCaribbean.getDisplayName(),
                locCentralAmerica.getDisplayName(),
                locSouthAmerica.getDisplayName(),
                locNorthernAmerica.getDisplayName() };

        //Asia
        Locale locCentralAsia = new Locale("", "143");
        Locale locEasternAsia = new Locale("", "030");
        Locale locSouthEasternAsia = new Locale("", "035");
        Locale locSouthernAsia = new Locale("", "034");
        Locale locWesternAsia = new Locale("", "145");

        String[] regionAsia = new String[] {
                locCentralAsia.getDisplayName(),
                locEasternAsia.getDisplayName(),
                locSouthEasternAsia.getDisplayName(),
                locSouthernAsia.getDisplayName(),
                locWesternAsia.getDisplayName() };

        //Europe
        Locale locEasternEurope = new Locale("", "151");
        Locale locNorthernEurope = new Locale("", "154");
        Locale locSouthernEurope = new Locale("", "039");
        Locale locWesternEurope = new Locale("", "155");

        String[] regionEurope = new String[] {
                locEasternEurope.getDisplayName(),
                locNorthernEurope.getDisplayName(),
                locSouthernEurope.getDisplayName(),
                locWesternEurope.getDisplayName() };

        //Oceania
        Locale locAustraliaNewZealand = new Locale("", "053");
        Locale locMelanesia = new Locale("", "054");
        Locale locMicronesia = new Locale("", "057");
        Locale locPolynesia = new Locale("", "061");

        String[] regionOceania = new String[] {
                locAustraliaNewZealand.getDisplayName(),
                locMelanesia.getDisplayName(),
                locMicronesia.getDisplayName(),
                locPolynesia.getDisplayName() };


        //fill group collection
        regionsData = new ArrayList<Map<String, String>>();
        for (String group: regions){
            m = new HashMap<String, String>(); //attributes for each group
            m.put("region", group);
            regionsData.add(m);
        }

        String[] groupFrom = new String[]{"region"};
        int[] groupTo = new int[]{android.R.id.text1};

        africaData = new ArrayList<Map<String, String>>();
        americasData = new ArrayList<Map<String, String>>();
        asiaData = new ArrayList<Map<String, String>>();
        europeData = new ArrayList<Map<String, String>>();
        oceaniaData = new ArrayList<Map<String, String>>();
        allChildData = new ArrayList<ArrayList<Map<String, String>>>();

        //africa subregions
        for (String child: regionAfrica){
            m = new HashMap<String, String>();
            m.put("subregion", child);
            africaData.add(m);
        }
        allChildData.add(africaData);

        //americas subregions
        for (String child: regionAmericas){
            m = new HashMap<String, String>();
            m.put("subregion", child);
            americasData.add(m);
        }
        allChildData.add(americasData);

        //asia subregions
        for (String child: regionAsia){
            m = new HashMap<String, String>();
            m.put("subregion", child);
            asiaData.add(m);
        }
        allChildData.add(asiaData);

        //europe subregions
        for (String child: regionEurope){
            m = new HashMap<String, String>();
            m.put("subregion", child);
            europeData.add(m);
        }
        allChildData.add(europeData);

        //oceania subregions
        for (String child: regionOceania){
            m = new HashMap<String, String>();
            m.put("subregion", child);
            oceaniaData.add(m);
        }
        allChildData.add(oceaniaData);


        String[] childFrom = new String[]{"subregion"};
        int[] childTo = new int[]{android.R.id.text1};


        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                regionsData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                allChildData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo
        );

        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);





        for(String locale: regions){
            //Log.d(TAG, locale.getCountry() + " "+ locale.getDisplayCountry());
            //Locale loc = new Locale("", locale);

            Log.d(TAG, locale);

            //String regexp = "[0-9]{3}";
            //Pattern pattern = Pattern.compile(regexp, Pattern.DOTALL);
            //Matcher matcher = pattern.matcher(locale.getCountry());

            //if( matcher.find() ) {
            //  list.add(locale.getDisplayCountry());
            // Log.d(TAG, locale.getCountry() + " "+ locale.getDisplayCountry());
            //}


        }




        /*
        String[] locales = Locale.getISOCountries();
        for(String locale: locales){
            //Log.d(TAG, locale.getCountry() + " "+ locale.getDisplayCountry());
            Locale obj = new Locale("", locale);

            Log.d(TAG, obj.getCountry() + " "+ obj.getDisplayName());

            //String regexp = "[0-9]{3}";
            //Pattern pattern = Pattern.compile(regexp, Pattern.DOTALL);
            //Matcher matcher = pattern.matcher(locale.getCountry());

            //if( matcher.find() ) {
              //  list.add(locale.getDisplayCountry());
               // Log.d(TAG, locale.getCountry() + " "+ locale.getDisplayCountry());
            //}


        }
        */



    }
}
