package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DownloadProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadProgressFragment extends Fragment  implements View.OnClickListener
         {

    private static final String ARG_PARAM = "region";

    private OnFragmentInteractionListener mListener;
    private RegionModel region;
    private TextView tvDownload;
    private TextView tvCountry;
    private TextView tvProgress;
    private ProgressBar progressBar;



    public DownloadProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment DownloadProgressFragment.
     */
    public static DownloadProgressFragment newInstance(RegionModel region) {
        Log.d(Prefs.TAG, " DownloadProgressFragment");


        DownloadProgressFragment fragment = new DownloadProgressFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, region);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            region = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_progress, container, false);
        tvDownload = (TextView) view.findViewById(R.id.tvDownload);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);

        String progress = String.format("%d%%", region.getItemDownloadPercent());
        tvDownload.setText(R.string.download);
        tvCountry.setText(region.getRegionName());
        tvProgress.setText(progress);
        progressBar.setProgress(region.getItemDownloadPercent());
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        view.setOnClickListener(this);



        return view;
    }


    @Override
    public void onClick(View v) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" onClick" );
        DownloadDialog dialog = DownloadDialog.newInstance(region);
        dialog.show(getFragmentManager(), "DownloadDialog");



    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
