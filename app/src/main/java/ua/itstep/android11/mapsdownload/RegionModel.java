package ua.itstep.android11.mapsdownload;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maksim Baydala on 15/05/17.
 * Model for items in CountriesActivity RecyclerView.
 * implements Parcelable for ??
 */

public class RegionModel implements Parcelable {
    private int itemDownloadPercent;
    private int depth;
    private int id;
    private long downloadId;
    private long lastEmittedDownloadPercent = -1;
    private String regionName = "";
    private String itemDownloadUrl = "";
    private String itemDownloadRootUrl = "";
    private float currentFileSize;
    private float totalFileSize;
    private boolean isMapAvailable = false;
    private DownloadingStatus downloadingStatus;
    private ArrayList<RegionModel> children = new ArrayList<>();
    private RegionModel parent;


    public RegionModel(Parcel in) {
        super();
        if (in != null) {
            depth = in.readInt();
            regionName = in.readString();
            itemDownloadPercent = in.readInt();
            currentFileSize = in.readFloat();
            totalFileSize = in.readFloat();
            //parent = in.readParcelable(this); todo
        }
    }

    public RegionModel() {
        super();

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
        dest.writeFloat(currentFileSize);
        dest.writeFloat(totalFileSize);
        dest.writeList(children);
        if (null != parent) {
            //dest.writeParcelable(parent, 1); todo stackoverFlow
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }


    public float getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(float currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public float getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(float totalFileSize) {
        this.totalFileSize = totalFileSize;
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

    public List<RegionModel> getChildren() {
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

    public String getItemDownloadRootUrl() {
        return itemDownloadRootUrl;
    }

    public void setItemDownloadRootUrl(String itemDownloadRootUrl) {
        this.itemDownloadRootUrl = itemDownloadRootUrl;
    }
}
