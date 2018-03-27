package com.example.ajdin.probafragmenti.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajdin on 12/3/2017.
 */

//public class GetList extends AsyncTask {
//
//    private DbxClientV2 dbxClient;
//
//    private Context context;
//    private ArrayList<String> lista;
//    private List<Metadata> meta;
//
//    public GetList(DbxClientV2 dbxClient, Context context) {
//        this.dbxClient = dbxClient;
//
//        this.context = context;
//    }
//
//    @Override
//    protected Object doInBackground(Object[] params) {
//        try {
//
//            meta = dbxClient.files().listFolder("/Racuni").getEntries();
//
//
//            Log.d("Upload Status", "Success");
//        } catch (DbxException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    @Override
//    protected void onPostExecute(Object o) {
//        super.onPostExecute(o);
//        lista=new ArrayList<>();
//        for (Metadata m:meta) {
//            lista.add(m.getName());
//        }
//
//    }
//}