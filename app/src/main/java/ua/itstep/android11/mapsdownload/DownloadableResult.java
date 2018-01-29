package ua.itstep.android11.mapsdownload;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class DownloadableResult {

    private int percent;
    private int downloadStatus;
    private int alertReason;

    public int getAlertReason() {
        return alertReason;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setAlertReason(int alertReason) {
        this.alertReason = alertReason;
    }
}
