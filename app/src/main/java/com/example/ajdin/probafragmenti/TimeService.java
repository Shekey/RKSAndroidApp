//package com.example.ajdin.probafragmenti;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.dropbox.client2.DropboxAPI;
//import com.dropbox.client2.android.AndroidAuthSession;
//import com.dropbox.client2.exception.DropboxException;
//import com.dropbox.client2.session.AppKeyPair;
//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.v2.DbxClientV2;
//import com.dropbox.core.v2.files.ListFolderErrorException;
//import com.dropbox.core.v2.files.Metadata;
//import com.example.ajdin.probafragmenti.Activities.DropboxClient;
//
//import com.example.ajdin.probafragmenti.Activities.NameFragment;
//import com.example.ajdin.probafragmenti.Activities.UploadTask;
//import com.example.ajdin.probafragmenti.database.DatabaseHelper;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by ajdin on 27.3.2018..
// */
//
//public class TimeService extends Service {
//    // constant
//    public static final long NOTIFY_INTERVAL = 10 * 3000; // 10 seconds
//    public ArrayList<String> dropboxList=new ArrayList<>();
//
//    // run on another Thread to avoid crash
//    private Handler mHandler = new Handler();
//    // timer handling
//    private Timer mTimer = null;
//    private DbxClientV2 client;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        // cancel if already existed
//        if (mTimer != null) {
//            mTimer.cancel();
//        } else {
//            // recreate new
//            mTimer = new Timer();
//            DbxRequestConfig config = new DbxRequestConfig("dropbox/AjdinovProjekt", "en_US");
//            client = new DbxClientV2(config, "aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV");
//        }
//        // schedule task
//        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
//    }
//
//    class TimeDisplayTimerTask extends TimerTask {
//
//        private ArrayList<String> strings;
//
//        @Override
//        public void run() {
//            // run on another thread
//            mHandler.post(new Runnable() {
//
//                private ArrayList<String> lv_arr;
//
//                @Override
//                public void run() {
//                    new GetList(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"),TimeService.this).execute();
//                    if (dropboxList.size()!=0){
//
//                    }
//                }
//
//
//            });
//        }
//
//        private String getDateTime() {
//            // get date time in custom format
//            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
//            return sdf.format(new Date());
//        }
//        public List<Metadata> getListFile(String path) {
//
//            DbxRequestConfig config = new DbxRequestConfig("dropbox/AjdinovProjekt", "en_US");
//            DbxClientV2 client = new DbxClientV2(config, "aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV");
//            if (client == null) {
//                return null;
//            }
//
//            try {
//                return client.files().listFolder(path).getEntries();
//            } catch (DbxException e) {
//                return null;
//            }
//
//        }
////        public ArrayList<String> getLv_arr() throws DropboxException, DbxException {
////            ArrayList<String> fames = new ArrayList<String>();
////            DbxRequestConfig config = new DbxRequestConfig("dropbox/AjdinovProjekt", "en_US");
////            DbxClientV2 client = new DbxClientV2(config, "aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV");
////            if (client == null) {
////                return null;
////            }else {
////
////                int i = 0;
////                AppKeyPair appKeys = new AppKeyPair("h01r2htv5wd572y", "48g39vbfomaym0i");
////                AndroidAuthSession session = new AndroidAuthSession(appKeys);
////                DropboxAPI<AndroidAuthSession> mApi = new DropboxAPI<>(session);
////                List<Metadata> meta= client.files().listFolder("/Racuni").getEntries();
////                strings = new ArrayList<String>();
////                for (Metadata m:meta) {
////                    strings.add(m.getPathDisplay());
////
////                }
////
//////                DropboxAPI.Entry dirent = mApi.metadata("/Racuni", 1000, null, true, null);
//////                ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
//////                ArrayList<String> dir = new ArrayList<String>();
//////                for (DropboxAPI.Entry ent : meta) {
//////                    files.add(ent);// Add it to the list of thumbs we can choose from
//////                    //dir = new ArrayList<String>();
//////                    dir.add(new String(files.get(i++).path));
//////                }
//////                fnames = dir.toArray(new String[dir.size()]);
////            }
////
////            return fames=strings;
////        }
//
//    }
//    private ArrayList<String> getList() {
//
//        ArrayList<String> inFiles = new ArrayList<String>();
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "racuni");
//        File exportDir2 = new File(Environment.getExternalStorageDirectory(), "racunidevice");
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }
//        if (!exportDir2.exists())
//        {
//            exportDir2.mkdirs();
//        }
//        String path = Environment.getExternalStorageDirectory().toString()+"/racunidevice";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//
//        String[] files = directory.list();
//        Log.d("Files", "Size: "+ files.length);
//        int count=files.length-1;
//        while (count>0||count==0){
//            if (files[count].equals("Artikli.txt") || files[count].equals("desktop.ini")){
//                if (count == 0) {
//                    break;
//                }
//                --count;
//            }
//            inFiles.add(files[count--]);
//
//        }
//        ArrayList<String> kopija=new ArrayList<>();
//        for (String d:inFiles){
//            for (String dd:dropboxList){
//                if (d.equals(dd)){
//                    kopija.add(dd);
//                }
//            }
//        }
//
//        return kopija;
//    }
//    class GetList extends AsyncTask {
//
//        private DbxClientV2 dbxClient;
//
//        private Context context;
//        private ArrayList<String> lista;
//        private List<Metadata> meta;
//
//        public GetList(DbxClientV2 dbxClient, Context context) {
//            this.dbxClient = dbxClient;
//
//            this.context = context;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            try {
//
//                meta = dbxClient.files().listFolder("/Racuni").getEntries();
//
//
//                Log.d("Upload Status", "Success");
//            } catch (DbxException e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            lista=new ArrayList<>();
//            for (Metadata m:meta) {
//                lista.add(m.getName());
//            }
//            dropboxList=lista;
//            dropboxList=getList();
//
//
//
//        }
//    }
//}
