package ua.itstep.android11.mapsdownload;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public interface ItemDownloadCallback {

    void onDownloadEnqueued(RegionModel downloadableItem);

    void onDownloadStarted(RegionModel downloadableItem);

    void onDownloadCompleted();

    void onDownloadCanceled(RegionModel downloadableItem);
}
