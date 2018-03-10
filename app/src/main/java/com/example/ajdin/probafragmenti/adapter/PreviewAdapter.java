package com.example.ajdin.probafragmenti.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.model.PreviewModel;

import java.util.ArrayList;

/**
 * Created by ajdin on 10.3.2018..
 */

public class PreviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<PreviewModel> model;


    public PreviewAdapter(Context context, ArrayList<PreviewModel> model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
