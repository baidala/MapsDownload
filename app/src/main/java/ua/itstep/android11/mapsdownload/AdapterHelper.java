package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maksim Baydala on 04/05/17.
 */

public class AdapterHelper {
    final String ATTR_REGION = "region";
    final String ATTR_SUBREGION = "subregion";
    final String ATTR_CODE = "M49code";

    ArrayList<Map<String, String>> regionsData;
    ArrayList<Map<String, String>> africaData;
    ArrayList<Map<String, String>> americasData;
    ArrayList<Map<String, String>> asiaData;
    ArrayList<Map<String, String>> europeData;
    ArrayList<Map<String, String>> oceaniaData;
    ArrayList<ArrayList<Map<String, String>>> allChildData;
    Map<String, String> m;
    ExpandableListAdapter adapter;
    Context ctx;
    Locale locale;


    //regions UN M49 codes
    String[] regionCodes = new String[]{Prefs.regionAfrica, Prefs.regionAmericas, Prefs.regionAsia, Prefs.regionEurope, Prefs.regionOceania};

    //Africa
    String[] regionAfrica = new String[] {Prefs.regionNorthernAfrica, Prefs.regionEasternAfrica, Prefs.regionMiddleAfrica, Prefs.regionSouthernAfrica, Prefs.regionWesternAfrica };

    //Americas
    String[] regionAmericas = new String[] {Prefs.regionCaribbean, Prefs.regionCentralAmerica, Prefs.regionSouthAmerica, Prefs.regionNorthernAmerica };

    //Asia
    String[] regionAsia = new String[] {Prefs.regionCentralAsia, Prefs.regionEasternAsia, Prefs.regionSouthEasternAsia, Prefs.regionSouthernAsia, Prefs.regionWesternAsia};

    //Europe
    String[] regionEurope = new String[] { Prefs.regionEasternEurope, Prefs.regionNorthernEurope, Prefs.regionSouthernEurope, Prefs.regionWesternEurope};

    //Oceania
    String[] regionOceania = new String[] { Prefs.regionAustraliaNewZealand, Prefs.regionMelanesia, Prefs.regionMicronesia, Prefs.regionPolynesia };


    AdapterHelper(Context context){
        ctx = context;
    }

    ExpandableListAdapter getAdapter(){

        //fill group collection
        regionsData = new ArrayList<Map<String, String>>();
        for (String group: regionCodes){
            m = new HashMap<String, String>(); //attributes for each group
            locale = new Locale("", group);
            m.put(ATTR_REGION, locale.getDisplayName());
            regionsData.add(m);
        }



        africaData = new ArrayList<Map<String, String>>();
        americasData = new ArrayList<Map<String, String>>();
        asiaData = new ArrayList<Map<String, String>>();
        europeData = new ArrayList<Map<String, String>>();
        oceaniaData = new ArrayList<Map<String, String>>();
        allChildData = new ArrayList<ArrayList<Map<String, String>>>();

        //africa subregions
        for (String child: regionAfrica){
            m = new HashMap<String, String>();
            locale = new Locale("", child);
            m.put(ATTR_SUBREGION, locale.getDisplayName());
            m.put(ATTR_CODE, child);
            africaData.add(m);
        }
        allChildData.add(africaData);

        //americas subregions
        for (String child: regionAmericas){
            m = new HashMap<String, String>();
            locale = new Locale("", child);
            m.put(ATTR_SUBREGION, locale.getDisplayName());
            m.put(ATTR_CODE, child);
            americasData.add(m);
        }
        allChildData.add(americasData);

        //asia subregions
        for (String child: regionAsia){
            m = new HashMap<String, String>();
            locale = new Locale("", child);
            m.put(ATTR_SUBREGION, locale.getDisplayName());
            m.put(ATTR_CODE, child);
            asiaData.add(m);
        }
        allChildData.add(asiaData);

        //europe subregions
        for (String child: regionEurope){
            m = new HashMap<String, String>();
            locale = new Locale("", child);
            m.put(ATTR_SUBREGION, locale.getDisplayName());
            m.put(ATTR_CODE, child);
            europeData.add(m);
        }
        allChildData.add(europeData);

        //oceania subregions
        for (String child: regionOceania){
            m = new HashMap<String, String>();
            locale = new Locale("", child);
            Log.d(Prefs.TAG, "Adapter country = " + locale.getDisplayCountry());
            m.put(ATTR_SUBREGION, locale.getDisplayCountry());  //locale.getDisplayName()
            m.put(ATTR_CODE, child);
            oceaniaData.add(m);

        }

        //locale = new Locale("", "061");
        //Log.d(Prefs.TAG, "Adapter country = " + locale.getDisplayCountry());


        //m.clear(); do not clear !!!
        m = null;
        locale = null;
        allChildData.add(oceaniaData);


        String[] groupFrom = new String[]{ATTR_REGION};
        int[] groupTo = new int[]{android.R.id.text1};

        String[] childFrom = new String[]{ATTR_SUBREGION};
        int[] childTo = new int[]{android.R.id.text1};



        adapter = new ExpandableListAdapter(
                ctx,
                regionsData,
                R.layout.region_list_item,
                groupFrom,
                groupTo,
                allChildData,
                R.layout.region_list_item,
                childFrom,
                childTo
        );

        return adapter;

    } //getAdapter()

    String getChildCode(int groupPos, int childPos){
        String res = ((HashMap<String, String>)(adapter.getChild(groupPos, childPos))).get(ATTR_CODE);
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".getChildCode = " + res);

        return res;
    }



}
