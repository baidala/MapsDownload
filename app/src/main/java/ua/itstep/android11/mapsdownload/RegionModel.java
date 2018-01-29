package ua.itstep.android11.mapsdownload;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by Maksim Baydala on 15/05/17.
 * Model for items in CountriesActivity RecyclerView.
 * implements Parcelable for ??
 */

public class RegionModel implements Parcelable {
    private long downloadId;
    private String regionName = "";
    private int progress = 0;
    private long currentFileSize;
    private long totalFileSize;
    private String m49code = "";
    private boolean isLoaded = false;
    private boolean inProgress = false;
    private boolean isMapAvailable = false;
    private DownloadingStatus downloadingStatus;
    private String itemDownloadUrl;
    private int itemDownloadPercent;
    private long lastEmittedDownloadPercent = -1;
    private int depth;
    private int id;
    private ArrayList<RegionModel> children = new ArrayList<>();
    private RegionModel parent;



    public RegionModel(Parcel in) {
        super();
        if (in != null) {

            depth = in.readInt();
            regionName = in.readString();
            itemDownloadPercent = in.readInt();
            currentFileSize = in.readInt();
            totalFileSize = in.readInt();
            m49code = in.readString();
            isLoaded = in.readByte() != 0;
            inProgress = in.readByte() != 0;
            //parent = in.readParcelable(this); todo
        }
    }

    public RegionModel() {
        super();
        //children = new ArrayList<>();
    }

    public static final Creator<RegionModel> CREATOR = new Creator<RegionModel>() {
        @Override
        public RegionModel createFromParcel(Parcel in) {
            return new RegionModel(in);
        }

        @Override
        public RegionModel[] newArray(int size) {
            return new RegionModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(depth);
        dest.writeString(regionName);
        dest.writeInt(progress);
        dest.writeLong(currentFileSize);
        dest.writeLong(totalFileSize);
        dest.writeString(m49code);
        dest.writeByte((byte) (isLoaded ? 1 : 0));
        dest.writeByte((byte) (inProgress ? 1 : 0));
        dest.writeList(children);
        if (null != parent) {
            //dest.writeParcelable(parent, 1); todo stackoverFlow
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getProgress(){
        return this.progress;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    public String getM49code() {
        return this.m49code;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public void setIsLoaded(boolean isLoading) {
        this.isLoaded = isLoading;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setProgress(int progress) {
        if(progress > 0){
            this.progress = progress;
        }

    }

    public long getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(long currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void setM49code(String m49code) {
        this.m49code = m49code;
    }

    public DownloadingStatus getDownloadingStatus() {
        return downloadingStatus;
    }

    public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
    }

    public long getLastEmittedDownloadPercent() {
        return lastEmittedDownloadPercent;
    }

    public void setLastEmittedDownloadPercent(long lastEmittedDownloadPercent) {
        this.lastEmittedDownloadPercent = lastEmittedDownloadPercent;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void addChildren(RegionModel region) {
        children.add(region);
    };

    public ArrayList<RegionModel> getChildren() {
        return children;
    };

    public int getChildrenCount() { return children.size(); }

    public int getItemDownloadPercent() {
        return itemDownloadPercent;
    }

    public void setItemDownloadPercent(int percent) {
        this.itemDownloadPercent = percent;
    }


    public boolean isMapAvailable() {
        return isMapAvailable;
    }

    public void setMapAvailable(boolean mapAvailable) {
        this.isMapAvailable = mapAvailable;
    }

    public RegionModel getParent() {
        return parent;
    }

    public void setParent(RegionModel parent) {
        this.parent = parent;
    }

    public String getItemDownloadUrl() {
        return itemDownloadUrl;
    }

    public void setItemDownloadUrl(String itemDownloadUrl) {
        this.itemDownloadUrl = itemDownloadUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
