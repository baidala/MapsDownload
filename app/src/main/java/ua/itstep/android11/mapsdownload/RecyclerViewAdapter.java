package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maksim Baydala on 04/05/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final static String TAG = "MAPS >>>>>>>>";
    final String ATTR_COUNTRY = "country";
    final String ATTR_CODE = "M49code";

    Context context;
    ArrayList<Map<String, String>> regionData;
    Map<String, String> item;
    Locale locale;
    String[] regionCodes;


    /*
    //subregions UN M49 codes
    String[] regionCodes = new String[]{
            "015", "014", "017", "018", "011", "029",
            "013", "005", "021", "143", "030", "035",
            "034", "145", "151", "154", "039", "155",
            "053", "054", "057", "061" };
    */

    // country UN M49 codes
    //africa
    String[] regionNorthernAfrica = new String[] { "DZA", "EGY", "LBY", "MAR", "SDN", "TUN", "ESH" };
    String[] regionEasternAfrica = new String[] { "IOT", "BDI", "COM", "DJI", "ERI", "ETH", "ATF", "KEN", "MDG", "MWI", "MUS", "MYT", "MOZ", "REU", "RWA", "SYC", "SOM", "SSD", "UGA", "TZA", "ZMB", "ZWE" };
    String[] regionMiddleAfrica = new String[] { "AGO", "CMR", "CAF", "TCD", "COG", "COD", "GNQ", "GAB", "STP" };
    String[] regionSouthernAfrica = new String[] { "BWA", "LSO", "NAM", "ZAF", "SWZ" };
    String[] regionWesternAfrica = new String[] { "BEN", "BFA", "CPV", "CIV", "GMB", "GHA", "GIN", "GNB", "LBR", "MLI", "MRT", "NER", "NGA", "SHN", "SEN", "SLE", "TGO" };

    //americas
    String[] regionCaribbean = new String[] { "AIA", "ATG", "ABW", "BHS", "BRB", "BES", "VGB", "CYM", "CUB", "CUW", "DMA", "DOM", "GRD", "GLP", "HTI", "JAM", "MTQ", "MSR", "PRI", "BLM", "KNA", "LCA", "MAF", "VCT", "SXM", "TTO", "TCA", "VIR" };
    String[] regionCentralAmerica = new String[] { "BLZ", "CRI", "SLV", "GTM", "HND", "MEX", "NIC", "PAN" };
    String[] regionSouthAmerica = new String[] { "ARG", "BOL", "BVT", "BRA", "CHL", "COL", "ECU", "FLK", "GUF", "GUY", "PRY", "PER", "SGS", "SUR", "URY", "VEN" };
    String[] regionNorthernAmerica = new String[] { "BMU", "CAN", "GRL", "SPM", "USA" };

    //asia
    String[] regionCentralAsia = new String[] { "KAZ", "KGZ", "TJK", "TKM", "UZB" };
    String[] regionEasternAsia = new String[] { "CHN", "HKG", "MAC", "PRK", "JPN", "MNG", "KOR" };
    String[] regionSouthEasternAsia = new String[] { "BRN", "KHM", "IDN", "LAO", "MYS", "MMR", "PHL", "SGP", "THA", "TLS", "VNM" };
    String[] regionSouthernAsia = new String[] { "AFG", "BGD", "BTN", "IND", "IRN", "MDV", "NPL", "PAK", "LKA" };
    String[] regionWesternAsia = new String[] { "ARM", "AZE", "BHR", "CYP", "GEO", "IRQ", "ISR", "JOR", "KWT", "LBN", "OMN", "QAT", "SAU", "PSE", "SYR", "TUR", "ARE", "YEM" };

    //europe
    String[] regionEasternEurope = new String[] { "BLR", "BGR", "CZE", "HUN", "POL", "MDA", "ROU", "RUS", "SVK", "UKR" };
    String[] regionNorthernEurope = new String[] { "ALA", "GGY", "JEY", "DNK", "EST", "FRO", "FIN", "ISL", "IRL", "IMN", "LVA", "LTU", "NOR", "SJM", "SWE", "GBR" };
    String[] regionSouthernEurope = new String[] { "ALB", "AND", "BIH", "HRV", "GIB", "GRC", "VAT", "ITA", "MLT", "MNE", "PRT", "SMR", "SRB", "SVN", "ESP", "MKD" };
    String[] regionWesternEurope = new String[] { "AUT", "BEL", "FRA", "DEU", "LIE", "LUX", "MCO", "NLD", "CHE" };

    //oceania
    String[] regionAustraliaNewZeland = new String[] { "AUS", "CXR", "CCK", "HMD", "NZL", "NFK" };
    String[] regionMelanesia = new String[] { "FJI", "NCL", "PNG", "SLB", "VUT" };
    String[] regionMicronesia = new String[] { "GUM", "KIR", "MHL", "FSM", "NRU", "MNP", "PLW", "UMI" };
    String[] regionPolynesia = new String[] { "ASM", "COK", "PYF", "NIU", "PCN", "WSM", "WSM", "TON", "TUV", "WLF" };



    public RecyclerViewAdapter(Context context, String code) {
        Log.d(TAG, getClass().getSimpleName() +".CONSTRUCT ");

        this.context = context;

        regionData = new ArrayList<Map<String, String>>();

        switch (code) {
            case "015":
                regionCodes = regionNorthernAfrica;
                break;
            case "014":
                regionCodes = regionEasternAfrica;
                break;
            case "017":
                regionCodes = regionMiddleAfrica;
                break;
            case "018":
                regionCodes = regionSouthernAfrica;
                break;
            case "011":
                regionCodes = regionWesternAfrica;
                break;
            case "029":
                regionCodes = regionCaribbean;
                break;
            case "013":
                regionCodes = regionCentralAmerica;
                break;
            case "005":
                regionCodes = regionSouthAmerica;
                break;
            case "021":
                regionCodes = regionNorthernAmerica;
                break;
            case "143":
                regionCodes = regionCentralAsia;
                break;
            case "030":
                regionCodes = regionEasternAsia;
                break;
            case "035":
                regionCodes = regionSouthEasternAsia;
                break;
            case "034":
                regionCodes = regionSouthernAsia;
                break;
            case "145":
                regionCodes = regionWesternAsia;
                break;
            case "151":
                regionCodes = regionEasternEurope;
                break;
            case "154":
                regionCodes = regionNorthernEurope;
                break;
            case "039":
                regionCodes = regionSouthernEurope;
                break;
            case "155":
                regionCodes = regionWesternEurope;
                break;
            case "053":
                regionCodes = regionAustraliaNewZeland;
                break;
            case "054":
                regionCodes = regionMelanesia;
                break;
            case "057":
                regionCodes = regionMicronesia;
                break;
            case "061":
                regionCodes = regionPolynesia;
                break;

        }



        for (String child: regionCodes){
            item = new HashMap<String, String>();
            locale = new Locale("", child);
            Log.d(TAG, "country = " + locale.getDisplayName());
            item.put(ATTR_COUNTRY, locale.getDisplayName());
            item.put(ATTR_CODE, child);
            regionData.add(item);
        }

        locale = new Locale("", "155");
        Log.d(TAG, "CCCCCcountry = " + locale.toString());


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, getClass().getSimpleName() +".onBindViewHolder() "+ position);

        holder.imageView.setImageResource(R.drawable.ic_map);
        holder.tvCountry.setText(regionData.get(position).get(ATTR_COUNTRY));
        holder.btnImport.setBackgroundResource(R.drawable.ic_action_import);
    }

    @Override
    public int getItemCount() {
        return regionData.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView tvCountry;
        private Button btnImport;
        private ImageView imageView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            tvCountry = (TextView) view.findViewById(R.id.tvCountry);
            btnImport = (Button) view.findViewById(R.id.btnImport);

            btnImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, getClass().getSimpleName() +".onClick ");
                }
            });

            Log.d(TAG, getClass().getSimpleName() +"  constructor () ");

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCountry.getText() + "'";
        }
    }


} //RecyclerViewAdapter
