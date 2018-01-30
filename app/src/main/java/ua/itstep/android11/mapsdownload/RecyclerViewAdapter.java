package ua.itstep.android11.mapsdownload;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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


    private Context context;
    private ArrayList<RegionModel> itemModelsList;





    public RecyclerViewAdapter(Context ctx, RegionModel region, OnFragmentInteractionListener listener, RecyclerView recyclerView) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".CONSTRUCT ");

        this.mListener = listener;
        this.context = ctx;
        this.recyclerView = recyclerView;

        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        //Observable for percent of individual downloads.
        mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
        //Observable for download request
        mDownloadRequestsSubscriber = new DownloadRequestsSubscriber(this);

        itemModelsList = region.getChildren();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(Prefs.TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);
        ItemDetailsViewHolder viewHolder = new ItemDetailsViewHolder(view,  context, this, mListener);
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +".onBindViewHolder() "+ position);

        RegionModel item = itemModelsList.get(position);
        item.setId(position);

        if (!(holder instanceof ItemDetailsViewHolder)) {
            return;
        }

        ((ItemDetailsViewHolder) holder).updateDetails(item);
    }

    @Override
    public int getItemCount() {
        return  ( itemModelsList != null ? itemModelsList.size() : 0 );
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
        currentDownloadsCount--;
        mDownloadRequestsSubscriber.requestFile(Prefs.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
                currentDownloadsCount);
    }

    @Override
    public void onDownloadCanceled(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onDownloadCanceled" );
        currentDownloadsCount--;
        downloadableItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
        RxDownloadManagerHelper.cancelDownload(downloadManager, downloadableItem,
                mItemDownloadPercentObserver.getPercentageObservableEmitter());
        mListener.onItemDownloaded(downloadableItem);

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
        notifyItemChanged(downloadableItem.getId()); //onBindViewHolder
        mListener.onItemDownloaded(downloadableItem);

    }


} //RecyclerViewAdapter
