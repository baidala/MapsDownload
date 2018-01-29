package ua.itstep.android11.mapsdownload;

import android.net.Uri;

/**
 * Created by Maksim Baydala on 27/01/18.
 */

public interface OnFragmentInteractionListener {
    void onItemSelected(RegionModel region);
    void onItemDownloaded(RegionModel region);
}
