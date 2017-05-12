package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maksim Baydala on 04/05/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    final String ATTR_COUNTRY = "country";
    final String ATTR_CODE = "M49code";

    DownloadTask downloadTask;
    CountriesActivity activity;

    Context context;
    ArrayList<Map<String, String>> regionData;
    boolean[] itemDownloadFlags;
    Map<String, String> item;
    Map<String, String> mapUrl;
    Locale locale;
    String[] regionCodes;
    //ViewHolder vHolder;


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


    String DEU_URL = "http://download.osmand.net/download.php?standard=yes&file=Denmark_europe_2.obf.zip";


    public RecyclerViewAdapter(CountriesActivity activity, String code) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".CONSTRUCT ");

        this.context = activity.getApplicationContext();
        this.activity = activity;

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

        itemDownloadFlags = new boolean[regionCodes.length];

        for (int i = 0; i < regionCodes.length; i++){
            itemDownloadFlags[i] = false ;
            item = new HashMap<String, String>();
            locale = new Locale("", regionCodes[i]);
            Log.d(Prefs.TAG, "country = " + locale.getDisplayName());
            item.put(ATTR_COUNTRY, locale.getDisplayName());
            item.put(ATTR_CODE, regionCodes[i]);
            regionData.add(item);
        }

        mapUrl = new HashMap<String, String>();
        mapUrl.put("DEU",DEU_URL);


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Log.d(Prefs.TAG, getClass().getSimpleName() +".onBindViewHolder() "+ position);

        //downloadTask = new DownloadTask(context, holder);

        holder.m49code = regionData.get(position).get(ATTR_CODE);
        holder.imageView.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorItemIcon), PorterDuff.Mode.SRC_IN);
        holder.tvCountry.setText(regionData.get(position).get(ATTR_COUNTRY));
        Log.d(Prefs.TAG, getClass().getSimpleName() +".onBindViewHolder() " + position + "  "+ isItemDownloading(position));
        if (isItemDownloading(position)) {
            holder.imageDownload.setImageResource(R.drawable.ic_action_remove_dark);
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }

        holder.imageDownload.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorItemIcon), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public int getItemCount() {
        return ( null != regionData ? regionData.size() : 0 );
    }

    private DownloadTask getDownloadTask(ViewHolder holder){
        return (downloadTask != null) ? downloadTask : new DownloadTask(context, holder);
    }

    private void startAction(ViewHolder holder, int pos) {
        if ( isItemDownloading(pos) )  {
            cancelDownload(holder, pos);
        } else {
            startDownload(holder, pos);

        }
    }
    private void startDownload(ViewHolder holder, int pos){
        String m49code = regionData.get(pos).get(ATTR_CODE);
        setItemDownloading(pos, true);
        getDownloadTask(holder).execute(mapUrl.get(m49code));
    }

    private void cancelDownload(ViewHolder holder, int pos){
        Log.d(Prefs.TAG, " cancelDownload: "+ pos);
        setItemDownloading(pos, false);
        getDownloadTask(holder).cancel(true);

    }

    private boolean isItemDownloading(int pos) {
        return itemDownloadFlags[pos];
    }

    private void setItemDownloading(int pos, boolean isLoading){
        itemDownloadFlags[pos] = isLoading;
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCountry;
        private ImageView imageView;
        private ImageView imageDownload;
        private ProgressBar progressBar;
        private String m49code;


        ViewHolder(final View view) {
            super(view);
            //mView = view;
            imageView = (ImageView) view.findViewById(R.id.imageIcon);
            tvCountry = (TextView) view.findViewById(R.id.tvCountry);
            imageDownload = (ImageView) view.findViewById(R.id.imageDownload);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            //downloadTask = new DownloadTask(view.getContext(), this);


            imageDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.onClick " + getAdapterPosition());

                    startAction(ViewHolder.this, getAdapterPosition());
                    //downloadTask = new DownloadTask(view.getContext(), ViewHolder.this);

                }
            });

            Log.d(Prefs.TAG, getClass().getSimpleName() +"  ViewHolder.constructor () ");

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCountry.getText() + "'";
        }
    }



    private class DownloadTask extends AsyncTask<String, Integer, String > {
        private final int SIZE = 4096 ;
        private final String CONNECTION = "Connection";
        private final String KEEP_ALIVE = "Keep-Alive";

        private Context context;
        private PowerManager.WakeLock wakeLock;
        private ViewHolder vHolder;

        DownloadTask(Context ctx, ViewHolder holder){
            this.context = ctx;
            this.vHolder = holder;

        }

        DownloadTask(){}

        @Override
        protected String doInBackground(String... url) {
            Log.d(Prefs.TAG, "doInBackground url: " + url[0]);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection conn = null;

            try {
                conn = (HttpURLConnection) new URL(url[0]).openConnection();
                conn.setDoInput(true);
                conn.setRequestProperty(CONNECTION, KEEP_ALIVE);
                conn.connect();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    StringBuilder str = new StringBuilder();
                    str.append("Server response: ");
                    str.append(conn.getResponseCode());
                    str.append(" ");
                    str.append(conn.getResponseMessage());
                    Log.d(Prefs.TAG, getClass().getSimpleName() +" doInBackground: " + str.toString());
                    return str.toString();
                }

                int fileLength = conn.getContentLength(); //might be -1  = no response
                Log.d(Prefs.TAG, getClass().getSimpleName() +" doInBackground fileLength: " + fileLength);
                File dest = new File( context.getFilesDir(), new File(url[0]).getName() );
                inputStream = new BufferedInputStream(conn.getInputStream());
                outputStream = new FileOutputStream(dest);

                byte[] data = new byte[SIZE];
                long total = 0;
                int count;
                while ( (count = inputStream.read(data)) != -1 ) {
                    if (isCancelled()) {
                        inputStream.close();
                        outputStream.flush();
                        outputStream.close();
                        conn.disconnect();
                        inputStream = null;
                        conn = null;
                        return null;
                    }

                    total += count;
                    if (fileLength > 0) publishProgress( (int)(total * 100 / fileLength) ); //onProgressUpdate called

                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                conn.disconnect();
                inputStream = null;
                outputStream = null;
                conn = null;


            } catch (MalformedURLException ex) {
                Log.e(Prefs.TAG, "Parsing URL failed: "+ url);
                // for testing only
                long total = 100000;
                int count= 0;
                while (count < total){
                    publishProgress( (int)(count * 100 / total) );
                    count++;
                    Log.d(Prefs.TAG, "count: " +count);
                }

            } catch (IOException ex) {
                Log.d(Prefs.TAG, "URL does not exist: "+ url);
            } catch (OutOfMemoryError ex) {
                Log.w(Prefs.TAG, "Warning: Out of Memory! URL: "+ url);
            } finally {
                try {
                    if (outputStream != null) outputStream.close();
                    if (inputStream != null) inputStream.close();
                } catch (IOException ex) {}

                if (conn != null) conn.disconnect();





            }



            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(Prefs.TAG, " onPreExecute");
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();

            activity.progressBar.setIndeterminate(false);
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.VISIBLE);
            activity.tvCountry.setText(regionData.get(vHolder.getAdapterPosition()).get(ATTR_COUNTRY));
            activity.frameLayout.setVisibility(View.VISIBLE);

            vHolder.imageView.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorItemIcon), PorterDuff.Mode.SRC_IN);
            vHolder.progressBar.setProgress(0);
            vHolder.progressBar.setIndeterminate(false);
            vHolder.progressBar.setVisibility(View.VISIBLE);
            vHolder.imageDownload.setImageResource(R.drawable.ic_action_remove_dark);
            vHolder.imageDownload.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorItemIcon), PorterDuff.Mode.SRC_IN);
            //setItemDownloading(vHolder.getAdapterPosition(), true);
            Log.d(Prefs.TAG, " onPreExecute: " + vHolder.getAdapterPosition()+" "+ isItemDownloading(vHolder.getAdapterPosition()));

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(Prefs.TAG, " onProgressUpdate "+ values[0]);
            activity.tvCountry.setText(regionData.get(vHolder.getAdapterPosition()).get(ATTR_COUNTRY));
            String progress = values[0].toString() + " %";
            activity.tvProgress.setText(progress);
            activity.progressBar.setProgress(values[0]);
            activity.frameLayout.setVisibility(View.VISIBLE);
            vHolder.progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(Prefs.TAG, " onPostExecute: "+ s);
            vHolder.progressBar.setVisibility(View.GONE);
            vHolder.imageDownload.setImageResource(R.drawable.ic_action_import);
            vHolder.imageDownload.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorItemIcon), PorterDuff.Mode.SRC_IN);
            vHolder.imageView.getDrawable().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

            activity.frameLayout.setVisibility(View.GONE);
            //setItemDownloading(vHolder.getAdapterPosition(), false);
        }


    } //DownloadTask


} //RecyclerViewAdapter
