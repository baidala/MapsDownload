package ua.itstep.android11.mapsdownload;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import io.reactivex.ObservableEmitter;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class RxDownloadManagerHelper {

    private final static int DOWNLOAD_QUERY_DELAY_PARAM = 200;
    private final static int DOWNLOAD_COMPLETE_PERCENT = 100;
    private final static int PERCENT_MULTIPLIER = 100;
    private final static int MIN_DOWNLOAD_PERCENT_DIFF = 1;
    private final static int INVALID_DOWNLOAD_ID = -1;
    private final static String CLASS = RxDownloadManagerHelper.class.getSimpleName();
    private final static String BASE_URL = "http://download.osmand.net/download.php?standard=yes&file=";


    //http://developer.android.com/images/home/android-jellybean.png


    /**
     * @param downloadManager - Android's Download Manager.
     * @param downloadUrl     - The url(filename) of the item to be downloaded.
     * @return - the download id of the download.
     */
    public static long enqueueDownload(DownloadManager downloadManager,
                                       String downloadUrl) {
        if (downloadManager == null || downloadUrl == null || downloadUrl.equals("")) {
            return INVALID_DOWNLOAD_ID;
        }
        String url = BASE_URL + downloadUrl;

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadUrl);
        request.setDescription(downloadUrl);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadUrl);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }

    /**
     *
     * This method will be called upon every 'x' milliseconds to know the percentage of a download.
     * This method will only emit the percent download, if the current percent download is 5%
     * greater than the previous percent download.
     *
     * @param downloadManager
     * @param downloadableItem
     * @param percentFlowableEmiitter
     */
    public static void queryDownloadPercents(final DownloadManager downloadManager,
                                             final RegionModel downloadableItem,
                                             final ObservableEmitter percentFlowableEmiitter) {

        Log.d(Prefs.TAG, CLASS +  "  queryDownloadPercents");
        //If the emitter has been disposed, then return.
        if (downloadManager == null || downloadableItem == null || percentFlowableEmiitter == null
                || percentFlowableEmiitter.isDisposed()) {
            return;
        }

        long lastEmittedDownloadPercent = downloadableItem.getLastEmittedDownloadPercent();


        DownloadableResult downloadableResult = getDownloadResult(downloadManager, downloadableItem
                .getDownloadId());

        if (downloadableResult == null) {
            return;
        }

        downloadableItem.setCurrentFileSize(downloadableResult.getBytesDownloaded());
        downloadableItem.setTotalFileSize(downloadableResult.getBytesTotal());

        //Get the current DownloadPercent and download status
        int currentDownloadPercent = downloadableResult.getPercent();
        int downloadStatus = downloadableResult.getDownloadStatus();
        int alertReason = downloadableResult.getAlertReason();
        downloadableItem.setItemDownloadPercent(currentDownloadPercent);
        if ((currentDownloadPercent - lastEmittedDownloadPercent >= MIN_DOWNLOAD_PERCENT_DIFF) ||
                currentDownloadPercent == DOWNLOAD_COMPLETE_PERCENT) {
            percentFlowableEmiitter.onNext(downloadableItem);
            downloadableItem.setLastEmittedDownloadPercent(currentDownloadPercent);
        }
        Log.d(Prefs.TAG, CLASS +
                " Querying the DB: DownloadStatus =  " + downloadStatus + " downloadPercent = " +
                        "" + currentDownloadPercent);
        Log.d(Prefs.TAG, CLASS + " Querying the DB: alertReason =  " + alertReason);

        Handler handler;
        switch (downloadStatus) {
            case DownloadManager.STATUS_FAILED:
                Log.d(Prefs.TAG, CLASS +" STATUS_FAILED" );
                downloadManager.remove(downloadableItem.getDownloadId());
                downloadableItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                percentFlowableEmiitter.onNext(downloadableItem); //ItemDownloadPercentObserver
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                Log.d(Prefs.TAG, CLASS +" STATUS_SUCCESSFUL" );
                break;

            case DownloadManager.STATUS_PENDING:
                Log.d(Prefs.TAG, CLASS +" STATUS_PENDING" );
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryDownloadPercents(downloadManager, downloadableItem, percentFlowableEmiitter);
                    }
                }, DOWNLOAD_QUERY_DELAY_PARAM);
                break;

            case DownloadManager.STATUS_RUNNING:
                Log.d(Prefs.TAG, CLASS +" STATUS_RUNNING" );
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryDownloadPercents(downloadManager, downloadableItem, percentFlowableEmiitter);
                    }
                }, DOWNLOAD_QUERY_DELAY_PARAM);
                break;

            case DownloadManager.STATUS_PAUSED:
                Log.d(Prefs.TAG, CLASS +" STATUS_PAUSED" );
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryDownloadPercents(downloadManager, downloadableItem, percentFlowableEmiitter);
                    }
                }, DOWNLOAD_QUERY_DELAY_PARAM);
                downloadableItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                downloadManager.remove(downloadableItem.getDownloadId());
                percentFlowableEmiitter.onNext(downloadableItem);
                break;

            case DownloadManager.ERROR_FILE_ERROR:
                Log.d(Prefs.TAG, CLASS +" ERROR_FILE_ERROR" );
                downloadableItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                downloadManager.remove(downloadableItem.getDownloadId());
                percentFlowableEmiitter.onNext(downloadableItem);
                break;
        }
    }

    /**
     * @param downloadManager - Android's DownloadManager
     * @param downloadId      - The downloadId for which the progress has to be fetched from the db.
     * @return
     */
    private static DownloadableResult getDownloadResult(DownloadManager downloadManager,
                                                        long downloadId) {
        Log.d(Prefs.TAG, CLASS +  "  getDownloadResult");
        //Create a query with downloadId as the filter.
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);


        //Create an instance of downloadable result
        DownloadableResult downloadableResult = new DownloadableResult();

        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor == null || !cursor.moveToFirst()) {
                return downloadableResult;
            }
            //Get the download percent
            float bytesDownloaded =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            float bytesTotal =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

            downloadableResult.setBytesDownloaded(bytesDownloaded);
            downloadableResult.setBytesTotal(bytesTotal);

            int downloadPercent = (int) ((bytesDownloaded / bytesTotal) * PERCENT_MULTIPLIER);
            if (downloadPercent <= Prefs.DOWNLOAD_COMPLETE_PERCENT) {
                downloadableResult.setPercent(downloadPercent);
            }
            //Get the download status
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int downloadStatus = cursor.getInt(columnIndex);
            downloadableResult.setDownloadStatus(downloadStatus);

            //Get the pending/pause reason
            columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int alertReason = cursor.getInt(columnIndex);
            downloadableResult.setAlertReason(alertReason);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return downloadableResult;
    }

    public static void cancelDownload(DownloadManager downloadManager, RegionModel item, final ObservableEmitter percentFlowableEmiitter) {
        Log.d(Prefs.TAG, CLASS +  "  cancelDownload");

        if (downloadManager == null ) {
            return;
        }

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(item.getDownloadId());

        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            while(cursor.moveToNext()) {
                downloadManager.remove(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        percentFlowableEmiitter.onNext(item);


    }
}
