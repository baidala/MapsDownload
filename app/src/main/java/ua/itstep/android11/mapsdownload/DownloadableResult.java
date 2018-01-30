package ua.itstep.android11.mapsdownload;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class DownloadableResult {

    private int percent;
    private int downloadStatus;
    private int alertReason;
    private float bytesDownloaded;
    private float bytesTotal;

    public int getAlertReason() {
        return alertReason;
    }

    public int getPercent() {
        return percent;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public float getBytesDownloaded() {
        return bytesDownloaded;
    }

    public float getBytesTotal() {
        return bytesTotal;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setAlertReason(int alertReason) {
        this.alertReason = alertReason;
    }

    public void setBytesDownloaded(float bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
    }

    public void setBytesTotal(float bytesTotal) {
        this.bytesTotal = bytesTotal;
    }
}
