package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class ItemDetailsViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener  {

    private final View mView;
    private RegionModel downloadableItem;
    private Context context;
    private ItemDownloadCallback callback;
    private TextView tvRegionName;
    private ImageView imageIcon;
    private ImageView imageDownload;
    private ProgressBar progressBar;
    //private String m49code ;
    private final OnFragmentInteractionListener listener;

    public ItemDetailsViewHolder(final View itemView, Context context, ItemDownloadCallback callback, OnFragmentInteractionListener lstnr) {
        super(itemView);


        this.mView = itemView;
        this.listener = lstnr;
        this.imageIcon = (ImageView) itemView.findViewById(R.id.imageIcon);
        this.tvRegionName = (TextView) itemView.findViewById(R.id.regionName);
        this.imageDownload = (ImageView) itemView.findViewById(R.id.imageDownload);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        this.imageDownload.setOnClickListener(this);
        this.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.mView.onClick " + getAdapterPosition());
                if (downloadableItem.getChildrenCount() > 0 && null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onItemSelected(downloadableItem);

                }
            }
        });
        this.context = context;
        this.callback = callback;
        //Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.pos: " + getAdapterPosition());



    }


    //start/stop downloading
    @Override
    public void onClick(View v) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.imageDownload.onClick " + getAdapterPosition());

        DownloadingStatus downloadingStatus = downloadableItem.getDownloadingStatus();
        Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.imageDownload.onClick =" + downloadableItem.getRegionName());
        Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.imageDownload.onClick =" + downloadingStatus.getDownloadStatus());
        //Only when the icon is in not downloaded state, then do the following.
        //Need check if file exist
        if (downloadingStatus == DownloadingStatus.NOT_DOWNLOADED || downloadingStatus == DownloadingStatus.DOWNLOADED) {
            Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.imageDownload.onClick WaitingState");
            setToWaitingState();
            callback.onDownloadEnqueued(downloadableItem);

        }
        else if (downloadingStatus == DownloadingStatus.WAITING || downloadingStatus == DownloadingStatus.IN_PROGRESS) {
            Log.d(Prefs.TAG, getClass().getSimpleName() +" ViewHolder.imageDownload.onClick NOT_DOWNLOADED");
            setToDefaultState();
            callback.onDownloadCanceled(downloadableItem);
        }



    }



    public void updateDetails(RegionModel item) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" updateDetails");

        this.downloadableItem = item;
        tvRegionName.setText(downloadableItem.getRegionName());

        if (downloadableItem.getDepth() > 2) {
            setToDefaultState();
            if (downloadableItem.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                setToCompletedState();
            } else if (downloadableItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS &&
                    downloadableItem.getItemDownloadPercent()
                            == Prefs.DOWNLOAD_COMPLETE_PERCENT) {
                setToCompletedState();
                callback.onDownloadCompleted();
            } else if (downloadableItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS) {
                setInProgressState();

            } else if (downloadableItem.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {
                setToDefaultState();
            }
            Log.d(Prefs.TAG, getClass().getSimpleName() +"  " + downloadableItem.getRegionName() + " map = " + downloadableItem.isMapAvailable());
        } else {
            setRootState();

        }





        /*
        imageName.setText(downloadableItem.getItemTitle());

        itemCoverIcon.setImageResource(downloadableItem.getItemCoverId());
        imageDownloadIcon.setItemId(downloadableItem.getDepth());
        imageDownloadIcon.updateDownloadingStatus(downloadableItem.getDownloadingStatus());

        if (downloadableItem.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
            setImageToCompletedState(downloadableItem.getDepth());
        } else if (downloadableItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS &&
                downloadableItem.getItemDownloadPercent()
                        == Constants.DOWNLOAD_COMPLETE_PERCENT) {
            setImageToCompletedState(downloadableItem.getDepth());
            callback.onDownloadComplete();
        } else if (downloadableItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS) {
            setImageInProgressState(downloadableItem.getItemDownloadPercent(), downloadableItem.getDepth());
        }*/



    }

    private void setRootState() {
        imageIcon.setImageResource(R.drawable.ic_world_globe_dark);
        imageDownload.setImageResource(R.drawable.ic_action_import);
        progressBar.setVisibility(View.GONE);
        imageDownload.setVisibility(View.GONE);

    }


    private void setToDefaultState() {
        imageIcon.setImageResource(R.drawable.ic_map);
        imageDownload.setImageResource(R.drawable.ic_action_import);
        progressBar.setVisibility(View.GONE);
        if(downloadableItem.isMapAvailable()) {
            imageDownload.setVisibility(View.VISIBLE);
        } else {  imageDownload.setVisibility(View.GONE); }
        /*if (!downloadableItem.getDepth().equalsIgnoreCase(itemId)) {
            return;
        }
        imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.WAITING);
        */
    }

    private void setToCompletedState() {
        imageIcon.getDrawable().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        downloadableItem.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
        progressBar.setVisibility(View.GONE);
        imageDownload.setImageResource(R.drawable.ic_action_import);
        imageDownload.setVisibility(View.VISIBLE);
        /*if (!downloadableItem.getDepth().equalsIgnoreCase(itemId)) {
            return;
        }
        imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.DOWNLOADED);
        */
    }

    private void setInProgressState() {
        imageDownload.setImageResource(R.drawable.ic_action_remove_dark);
        imageDownload.setVisibility(View.VISIBLE);
        progressBar.setProgress(downloadableItem.getItemDownloadPercent());
        progressBar.setVisibility(View.VISIBLE);
        Log.d(Prefs.TAG, getClass().getSimpleName() +" setInProgressState =" + downloadableItem.getItemDownloadPercent());

        /*
        if (!downloadableItem.getDepth().equalsIgnoreCase(itemId)) {
            return;
        }
        imageDownloadIcon.updateProgress(context, progress);
        imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.IN_PROGRESS);
        */
    }

    private void setToWaitingState() {
        downloadableItem.setDownloadingStatus(DownloadingStatus.WAITING);
        imageDownload.setImageResource(R.drawable.ic_action_remove_dark);
        imageDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + tvRegionName.getText() + "'";
    }


}
