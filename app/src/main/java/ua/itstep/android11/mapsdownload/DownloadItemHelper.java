package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class DownloadItemHelper {

    public static ArrayList getItems(Context context) {
        ArrayList<RegionModel> downloadableItems = new ArrayList<>();

        if (context == null) {
            return downloadableItems;
        }
/*
        Resources res = context.getResources();
        String[] imagesId = res.getStringArray(R.array.image_ids);
        String[] imagesDisplayNamesList = res.getStringArray(R.array.image_display_names_list);
        String[] imageDownloadUrlList = res.getStringArray(R.array.image_download_url_list);
        TypedArray imageDownloadCoverList = res.obtainTypedArray(R.array.image_download_cover_list);

        for (int i = 0; i < imagesId.length; i++) {
            RegionModel downloadableItem = new DownloadableItem();
            String itemId = imagesId[i];
            downloadableItem.setDepth(itemId);
            String downloadingStatus = getDownloadStatus(context, itemId);
            downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
            downloadableItem.setItemTitle(imagesDisplayNamesList[i]);
            downloadableItem.setItemDownloadUrl(imageDownloadUrlList[i]);
            downloadableItems.add(downloadableItem);
        }
        return downloadableItems;*/return null;
    }

    /**
     * This method returns the downloadable Item with the latest percent and downloading status
     * @param context
     * @param downloadableItem
     * @return
     */
    public static RegionModel getItem(Context context, RegionModel
            downloadableItem) {
        if (context == null || downloadableItem == null) {
            return downloadableItem;
        }
        //String downloadingStatus = DownloadItemHelper.getDownloadStatus(context, downloadableItem.getDepth());
        //int downloadPercent = DownloadItemHelper.getDownloadPercent(context, downloadableItem.getDepth());
        //downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
        //downloadableItem.setItemDownloadPercent(downloadPercent);
        return downloadableItem;
    }

    public static String getDownloadStatus(Context context, String itemId) {
        SharedPreferences preferences =
                context.getSharedPreferences(Prefs.SHARED_PREFERENCES, Context
                        .MODE_PRIVATE);
        return preferences.getString(Prefs.DOWNLOAD_PREFIX + itemId,
                DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus());
    }

    public static void persistItemState(Context context, RegionModel downloadableItem) {
        /*
        DownloadItemHelper.setDownloadPercent(context, downloadableItem.getDepth(),
                downloadableItem.getItemDownloadPercent());
        DownloadItemHelper.setDownloadStatus(context, downloadableItem.getDepth(),
                downloadableItem.getDownloadingStatus());*/
    }

    public static void setDownloadStatus(Context context, String itemId, DownloadingStatus
            downloadingStatus) {
        /*SharedPreferences preferences =
                context.getSharedPreferences(SyncStateContract.Constants.SHARED_PREFERENCES, Context
                        .MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SyncStateContract.Constants.DOWNLOAD_PREFIX + itemId, downloadingStatus.getDownloadStatus());
        editor.commit();*/
    }

    public static void setDownloadPercent(Context context, String itemId, int percent) {
        /*SharedPreferences preferences =
                context.getSharedPreferences(SyncStateContract.Constants.SHARED_PREFERENCES, Context
                        .MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.PERCENT_PREFIX + itemId, percent);
        editor.commit();*/
    }

    public static int getDownloadPercent(Context context, String itemId) {
        /*SharedPreferences preferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
                        .MODE_PRIVATE);
        return preferences.getInt(Constants.PERCENT_PREFIX + itemId, 0);*/return 0;
    }
}
