package com.example.ajdin.probafragmenti.Activities;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.adapter.PreviewAdapter;
import com.example.ajdin.probafragmenti.model.PreviewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends DialogFragment {


    public PreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_preview, container, false);
        ListView preview=view.findViewById(R.id.preview);
        Bundle bundle=getArguments();
        if (bundle!=null){
            ArrayList<PreviewModel> models=(ArrayList)bundle.getSerializable("listPreview");
            PreviewAdapter adapter = new PreviewAdapter(getContext().getApplicationContext(), R.layout.row_preview, models);
            preview.setAdapter(adapter);
        }


        return view;
    }

}
