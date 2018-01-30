package ua.itstep.android11.mapsdownload;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Maksim Baydala on 29/01/18.
 */

public class DownloadDialog extends DialogFragment {

    private static final String ARG_PARAM = "region";

    private RegionModel region;
    private TextView tvRegion;
    private TextView tvProgress;
    private ProgressBar progressBar;


    public static DownloadDialog newInstance(RegionModel region) {
        Log.d(Prefs.TAG, " DownloadProgressFragment");

        DownloadDialog fragment = new DownloadDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, region);
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadDialog() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            region = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.download_dialog, null, true);
        tvRegion = (TextView) view.findViewById(R.id.regionName);
        tvProgress = (TextView) view.findViewById(R.id.progress);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        StringBuilder progress = new StringBuilder(formatSize(region.getCurrentFileSize()));
        progress.append(" from ");
        progress.append(formatSize(region.getTotalFileSize()));

        tvProgress.setText(progress.toString());
        tvRegion.setText(region.getRegionName());
        progressBar.setProgress(region.getItemDownloadPercent());
        builder.setView(view)
                .setTitle(R.string.title_category_dialog)
                .setPositiveButton(R.string.close_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Prefs.DEBUG) Log.d(Prefs.TAG, getClass().getSimpleName() + " onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Prefs.DEBUG) Log.d(Prefs.TAG, getClass().getSimpleName() + " onPause ");
    }



    private String formatSize(float size) {

        float Kb = 1  * 1024;
        float Mb = Kb * 1024;
        float Gb = Mb * 1024;
        float Tb = Gb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";

        return "???";
    }

    private String floatForm (double d) {
        return new DecimalFormat("#.##").format(d);
    }


}
