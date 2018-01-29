package ua.itstep.android11.mapsdownload;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maksim Baydala on 04/05/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemDownloadCallback, ItemPercentCallback {

    private final OnFragmentInteractionListener mListener;
    private int currentDownloadsCount = 0;
    private final DownloadManager downloadManager;
    private final RecyclerView recyclerView;
    private final ItemDownloadPercentObserver mItemDownloadPercentObserver;
    private final DownloadRequestsSubscriber mDownloadRequestsSubscriber;
    //private final WeakReference<Context> contextWeakReference;
    final String ATTR_COUNTRY = "country";
    final String ATTR_CODE = "M49code";

    RegionModel downloadTask;
    CountriesActivity activity;

    Context context;
    //ArrayList<Map<String, String>> regionData;
    ArrayList<ItemDetailsViewHolder> holders;
    ArrayList<RegionModel> itemModelsList;
    boolean[] itemDownloadFlags;
    //Map<String, String> item;
    //private RegionModel itemData;
    Map<String, String> mapUrl;
    Locale locale;
    String[] regionCodes;
    //private final ArrayList<RegionModel> itemsList;
    //private ArrayList<? extends Parcelable> parcelableList;
    private PowerManager.WakeLock wakeLock;


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
    String[] regionAustraliaNewZealand = new String[] { "AUS", "CXR", "CCK", "HMD", "NZL", "NFK" };
    String[] regionMelanesia = new String[] { "FJI", "NCL", "PNG", "SLB", "VUT" };
    String[] regionMicronesia = new String[] { "GUM", "KIR", "MHL", "FSM", "NRU", "MNP", "PLW", "UMI" };
    String[] regionPolynesia = new String[] { "ASM", "COK", "PYF", "NIU", "PCN", "WSM", "WSM", "TON", "TUV", "WLF" };


    String DEU_URL = "http://download.osmand.net/download.php?standard=yes&file=Denmark_europe_2.obf.zip";



    public RecyclerViewAdapter(Context ctx, RegionModel region, OnFragmentInteractionListener listener, RecyclerView recyclerView) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".CONSTRUCT ");
        String code = "ERROR";
        this.mListener = listener;
        this.context = ctx;
        this.recyclerView = recyclerView;
        //this.activity = activity;

        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //this.contextWeakReference = new WeakReference(context);

        //Observable for percent of individual downloads.
        mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
        //Observable for download request
        mDownloadRequestsSubscriber = new DownloadRequestsSubscriber(this);

        itemModelsList = region.getChildren();
        holders = new ArrayList<ItemDetailsViewHolder>();


        mapUrl = new HashMap<String, String>();
        mapUrl.put("DEU",DEU_URL);

        registerReceiver();



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(Prefs.TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);
        ItemDetailsViewHolder viewHolder = new ItemDetailsViewHolder(view, /*contextWeakReference.get()*/ context, this, mListener);
        holders.add(viewHolder);
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onCreateViewHolder() size: "+holders.size());

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".onBindViewHolder() "+ position);

        if(holders.contains(holder)){
            Log.d(Prefs.TAG, getClass().getSimpleName() +".onBindViewHolder() holder is in list");
        }

        RegionModel item = itemModelsList.get(position);
        item.setId(position);

        if (!(holder instanceof ItemDetailsViewHolder)) {
            return;
        }
        /*DownloadableItem downloadableItem = DownloadItemHelper.getItem(contextWeakReference
                .get(), itemsList.get(position));*/
        ((ItemDetailsViewHolder) holder).updateDetails(item);

    }

    @Override
    public int getItemCount() {
        return  ( itemModelsList != null ? itemModelsList.size() : 0 );
            //return ( null != regionData ? regionData.size() : 0 );

    }



    /**
     * This callback is called when the user clicks on any item for download.
     *
     * @param downloadableItem - Item to be downloaded.
     */
    public void onDownloadEnqueued(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onDownloadEnqueued" );
        mDownloadRequestsSubscriber.emitNextItem(downloadableItem);
    }


    /**
     * This callback is called when the item starts getting downloaded.
     *
     * @param downloadableItem - Item to be downloaded.
     */
    @Override
    public void onDownloadStarted(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onDownloadStarted" );
        //Increment the current number of downloads by 1
        currentDownloadsCount++;
        String downloadUrl = downloadableItem.getItemDownloadUrl();
        long downloadId =
                RxDownloadManagerHelper.enqueueDownload(downloadManager, downloadUrl);
        if (downloadId == Prefs.INVLALID_ID) {
            return;
        }
        downloadableItem.setDownloadId(downloadId);
        downloadableItem.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
        updateDownloadableItem(downloadableItem);
        RxDownloadManagerHelper.queryDownloadPercents(downloadManager, downloadableItem,
                mItemDownloadPercentObserver.getPercentageObservableEmitter());

    }

    @Override
    public void onDownloadCompleted() {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onDownloadComplete" );
        //Decrement the current number of downloads by 1
        currentDownloadsCount--;
        mDownloadRequestsSubscriber.requestFile(Prefs.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
                currentDownloadsCount);
    }

    @Override
    public void onDownloadCanceled(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onDownloadCanceled" );
        //Decrement the current number of downloads by 1
        currentDownloadsCount--;
        downloadableItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
        RxDownloadManagerHelper.cancelDownload(downloadManager, downloadableItem,
                mItemDownloadPercentObserver.getPercentageObservableEmitter());
        mListener.onItemDownloaded(downloadableItem);

        //mDownloadRequestsSubscriber.requestFile(Prefs.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
         //       currentDownloadsCount);
    }


    public void performCleanUp() {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" performCleanUp");
        mItemDownloadPercentObserver.performCleanUp();
        mDownloadRequestsSubscriber.performCleanUp();
    }

    @Override
    public void updateDownloadableItem(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" updateDownloadableItem size= " + itemModelsList.size());


        ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder)
                recyclerView.findViewHolderForLayoutPosition(downloadableItem.getId());
        //It means that the viewholder is not currently displayed.
        if (itemDetailsViewHolder == null) {
            if (downloadableItem.getItemDownloadPercent() == Prefs.DOWNLOAD_COMPLETE_PERCENT) {
                downloadableItem.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                onDownloadCompleted();
            }
            return;
        }
        Log.d(Prefs.TAG, getClass().getSimpleName() +" updateDownloadableItem size= ");
        if (downloadableItem.getDownloadingStatus().equals(DownloadingStatus.NOT_DOWNLOADED)) {
            onDownloadCompleted();
        }
        //itemDetailsViewHolder.updateDetails(downloadableItem);
        notifyItemChanged(downloadableItem.getId()); //onBindViewHolder
        mListener.onItemDownloaded(downloadableItem);

    }



    public void init(int position ){
        //Log.d(Prefs.TAG, getClass().getSimpleName() +".init: "+position);
        //CountriesActivity activity = getActivity();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();

        activity.progressBar.setIndeterminate(false);
        activity.progressBar.setProgress(0);
        activity.progressBar.setVisibility(View.VISIBLE);
        activity.tvCountry.setText(itemModelsList.get(position).getRegionName());
        activity.frameLayout.setVisibility(View.VISIBLE);

    }

    public void refresh(int position ){
        //Log.d(Prefs.TAG, getClass().getSimpleName() +".refreshe: "+position);
        if (getItem(position).isInProgress()) {
            //CountriesActivity activity = getActivity();
            activity.tvProgress.setText(getItem(position).getProgress() + " %");
            activity.progressBar.setProgress(getItem(position).getProgress());
            activity.frameLayout.setVisibility(View.VISIBLE);
        } else {
            activity.frameLayout.setVisibility(View.GONE);
            activity.progressBar.setProgress(0);
            activity.progressBar.setIndeterminate(false);
        }
        notifyItemChanged(position);
        //notifyDataSetChanged();

    }



    private void startAction(RecyclerView.ViewHolder holder, int pos) {
        RegionModel item = itemModelsList.get(pos);
        if ( item.isInProgress() )  {
            cancelDownload(holder, pos);
        } else {
            startDownload(holder, pos);

        }
    }

    private void startDownload(RecyclerView.ViewHolder holder, int pos){
        RegionModel item = itemModelsList.get(pos);
        item.setInProgress(true);

        Intent intent = new Intent(this.context, null);
        intent.putExtra("filename", item.getRegionName()); //TODO convert to filename
        intent.putExtra("m49code", item.getM49code());
        intent.putExtra("position", pos);
        activity.startService(intent);


        refresh(pos);

    }

    private void cancelDownload(RecyclerView.ViewHolder holder, int pos){
        Log.d(Prefs.TAG, " cancelDownload: "+ pos);
        RegionModel item = itemModelsList.get(pos);
        item.setInProgress(false);
        item.setIsLoaded(false);
        item.setProgress(0);

        refresh(pos);

    }

    public void setItems(ArrayList<RegionModel> itemsList) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".setItems size: " +itemsList.size());
        itemModelsList = new ArrayList<RegionModel>();
        itemModelsList.addAll(itemsList);
    }

    public RegionModel getItem(int pos) {
        return itemModelsList.get(pos);
    }

    public ArrayList<RegionModel> getParcelableList() {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".getParcelableList ");
        return itemModelsList;
    }

    public CountriesActivity getActivity(){
        return  activity;
    }


    private void registerReceiver(){
        //LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Prefs.MESSAGE_PROGRESS);
        //bManager.registerReceiver(broadcastReceiver, intentFilter);

    }




/*
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO check m49code
            if(intent.getAction().equals(Prefs.MESSAGE_PROGRESS)){

                RegionModel download = intent.getParcelableExtra("download");

                int pos = regionCodes[];
                RegionModel item = itemModelsList.get(pos);
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
    */





} //RecyclerViewAdapter
