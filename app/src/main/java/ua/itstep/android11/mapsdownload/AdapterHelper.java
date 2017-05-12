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
    String[] regionCodes = new String[]{"002", "019", "142", "150", "009"};

    //Africa
    String[] regionAfrica = new String[] {"015", "014", "017", "018", "011" };

    //Americas
    String[] regionAmericas = new String[] {"029", "013", "005", "021" };

    //Asia
    String[] regionAsia = new String[] {"143", "030", "035", "034", "145"};

    //Europe
    String[] regionEurope = new String[] { "151", "154", "039", "155"};

    //Oceania
    String[] regionOceania = new String[] { "053", "054", "057", "061" };


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
            m.put(ATTR_SUBREGION, locale.getDisplayName());
            m.put(ATTR_CODE, child);
            oceaniaData.add(m);
            //locale = new Locale("", "DEU");
            //Log.d(TAG, "Adapter country = " + locale.getDisplayCountry());
        }
        allChildData.add(oceaniaData);


        String[] groupFrom = new String[]{ATTR_REGION};
        int[] groupTo = new int[]{android.R.id.text1};

        String[] childFrom = new String[]{ATTR_SUBREGION};
        int[] childTo = new int[]{android.R.id.text1};



        adapter = new ExpandableListAdapter(
                ctx,
                regionsData,
                R.layout.expandable_list_item,
                groupFrom,
                groupTo,
                allChildData,
                R.layout.region_list_item,
                childFrom,
                childTo
        );



        return adapter;
    }

    String getChildCode(int groupPos, int childPos){
        String res = ((Map<String, String>)(adapter.getChild(groupPos, childPos))).get(ATTR_CODE);
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".getChildCode = " + res);

        return res;
    }



}
