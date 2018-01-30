package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FreespaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreespaceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView tvDevMemory;
    private TextView tvFree;
    private TextView tvSize;
    private ProgressBar progressBar;
    private int usedSpace;

    private OnFragmentInteractionListener mListener;

    public FreespaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FreespaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FreespaceFragment newInstance(String param1, String param2) {
        FreespaceFragment fragment = new FreespaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freespace, container, false);

        tvDevMemory = (TextView) view.findViewById(R.id.tvDevMemory);
        tvDevMemory.setText(R.string.dev_memory);

        tvFree = (TextView) view.findViewById(R.id.tvFree);
        tvFree.setText(R.string.free);

        tvSize = (TextView) view.findViewById(R.id.tvSize);
        tvSize.setText( getAvailableInternalMemorySize() );

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(usedSpace);
        Log.d(Prefs.TAG, getClass().getSimpleName() + ".onCreateView  usedSpace = " + usedSpace);

        return view;
    }

    private String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long totalBlocks = stat.getBlockCount();
        long totalBytes = totalBlocks * blockSize;
        long availableBytes = availableBlocks * blockSize;
        usedSpace = 100 - (int)((availableBytes * 100 / totalBytes));
        return formatSize(availableBytes);
    }

    private String floatForm (double d) {
        return new DecimalFormat("#.##").format(d);
    }

    private String formatSize(long size) {

        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";

        return "???";
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
