package com.example.ajdin.probafragmenti;

import android.Manifest;
import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.ajdin.probafragmenti.Activities.DropboxClient;
import com.example.ajdin.probafragmenti.Activities.LoginActivity;
import com.example.ajdin.probafragmenti.Activities.MainAcitivityFragment;
import com.example.ajdin.probafragmenti.Activities.NameFragment;
import com.example.ajdin.probafragmenti.Activities.show_history;
import com.example.ajdin.probafragmenti.adapter.Cart;
import com.example.ajdin.probafragmenti.adapter.CartHelper;
import com.example.ajdin.probafragmenti.adapter.CartItemAdapter;
import com.example.ajdin.probafragmenti.constant.Constant;
import com.example.ajdin.probafragmenti.database.DatabaseHelper;
import com.example.ajdin.probafragmenti.model.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="Mainactivity" ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final Cart cart = CartHelper.getCart();
        final CartItemAdapter cartItemAdapter = new CartItemAdapter(MainActivity.this);
        cart.clear();
        cartItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Cart cart = CartHelper.getCart();
        final CartItemAdapter cartItemAdapter = new CartItemAdapter(MainActivity.this);
        cart.clear();
        cartItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},1);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#54a9e6")));
//        if (!tokenExists()){
//
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//
//            startActivity(intent);
//
//        }
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NameFragment(), "NOVI RAČUN");
        adapter.addFragment(new show_history(), "HISTORIJA RAČUNA");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0){
                    getSupportActionBar().setTitle("Početna");
                }
                else {
                    getSupportActionBar().setTitle("Historija računa");

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        int count=0;

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() >0) {
                MainAcitivityFragment fragment = new MainAcitivityFragment();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainfragment, fragment);
                getSupportFragmentManager().popBackStack(R.id.mainfragment,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.commit();
            } else {
                Toast.makeText(getBaseContext(), "Pritisnite jos jednom nazad za izlazak iz aplikacije.", Toast.LENGTH_SHORT).show();
            }

        }
        mBackPressed = System.currentTimeMillis();


    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        }
//            return false;
//            // Disable back button..............
//        }
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                ucitaj_artikle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean tokenExists() {
        SharedPreferences prefs = getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }
    public void ReadDatabase(Context ctx) {
        ArrayList<Product> p = new ArrayList<Product>();
        Product temp;

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/updatedata/artikli.txt");
        int count=0;

        try {
            String[] line;
            BigDecimal decimal;
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-16LE"));
//            BufferedReader br = new BufferedReader(new FileInputStream(file), "UTF-16LE"));
//            BufferedReader br ;= new BufferedReader(new FileReader(file));
            BufferedReader br ;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1250"));
            String values = br.readLine();
            while (values != null){
                String str = "";

                str +=values;
                line=values.split(";");
                count=Integer.valueOf(line[0]);
                String cijena =line[3];
                if(cijena.isEmpty()){
                    cijena="0.0";
                }
                cijena = cijena.replace(",", ".");
                decimal = BigDecimal.valueOf(Double.valueOf(cijena));

                if (line.length==6) {
                    temp = new Product("1", line[1], decimal, line[4], line[2], line[5]);
                    p.add(temp);// your default value
                    Log.d(TAG, "ReadDatabase: "+count);
                    count++;
                }
                values = br.readLine();

            }
            Log.d(TAG,"count je "+count);
            new InsertDataTask().execute(p);
            // BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("myFile.csv"));
//            CSVReader csvReader = new CSVReader(new InputStreamReader
//                    (new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().toString() + "/updatedata/Artikli.txt"), 8192 * 32)), ';');
//            try {
//                DatabaseHelper db = new DatabaseHelper(ctx);
//                int count = 0;
//
//                String[] line;
//                long timeStart = System.nanoTime();
//                while ((line = csvReader.readNext()) != null) {
//
//                    count++;
//                    BigDecimal decimal;
//
//                    try {
//                        Log.d(TAG,"count je "+count);
//                        String cijena = line[3];
//                        cijena = cijena.replace(",", ".");
//                        decimal = BigDecimal.valueOf(Double.valueOf(cijena));
//                        if (line.length==6) {
//                            temp = new Product("1", line[1], decimal, line[4], line[2], line[5]);
//                            p.add(temp);// your default value
//                        }
//                        else{
//
//                            temp = new Product("1", line[1], decimal, line[4], line[2], "0");
//                            p.add(temp);// your default value
//                        }
//                    } catch (NumberFormatException e) {
//                        decimal = BigDecimal.valueOf(0.0);
//                        temp = new Product("1", line[1], decimal, line[4], line[2], "0");
//                        p.add(temp);// your default value
//                    }
//
//
//                    //  db.InsertArtikal(line[4],line[2],line[1],cijena,null);
//
//                }
////                    if(count >= 150000){
////                        break;
//                new InsertDataTask().execute(p);
//////
////              db.InsertArtikal(p);
////
//
//                long timeEnd = System.nanoTime();
//                System.out.println("Count: " + count);
//                System.out.println("Time: " + (timeEnd - timeStart) * 1.0 / 1000000000 + " sec");
//                db.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        }

//        ArrayList<Product> p = new ArrayList<Product>();
//        Product temp;
//        String csvFile = Environment.getExternalStorageDirectory().toString() + "/updatedata/Artikli.txt";
//        File fileDir = new File(csvFile);
//        try {
//            // BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("myFile.csv"));
//            CSVReader csvReader = new CSVReader(new BufferedReader(
//                    new InputStreamReader(
//                            new FileInputStream(fileDir), "UTF8")),';');
//            DatabaseHelper db = new DatabaseHelper(ctx);
//            int count = 0;
//
//            String [] line;
//            long timeStart = System.nanoTime();
//            while ((line = csvReader.readNext()) != null) {
//                String[] country = line;
//
//
//                if (country[4].contains("8086")){
//                    Log.d(TAG,"count je "+count);
//                    count++;
//                }
//                count++;
//                BigDecimal decimal;
//
////                    try {
//                Log.d(TAG,"count je "+count);
//                String cijena = country[3];
//                cijena = cijena.replace(",", ".");
//                decimal = BigDecimal.valueOf(Double.valueOf(cijena));
//                if (country.length==6) {
//                    temp = new Product("1", country[1], decimal, country[4], country[2], country[5]);
//                    p.add(temp);// your default value
//                }
////                else{
////
////                    temp = new Product("1", country[1], decimal, country[4], country[2], "0");
////                    p.add(temp);// your default value
////                }
////                    } catch (NumberFormatException e) {
////                        decimal = BigDecimal.valueOf(0.0);
////                        temp = new Product("1", country[1], decimal, country[4], country[2], "0");
////                        p.add(temp);// your default value
////                    }
//
//
//                //  db.InsertArtikal(line[4],line[2],line[1],cijena,null);
//
//            }
////                    if(count >= 150000){
////                        break;
//            new InsertDataTask().execute(p);
//////
////              db.InsertArtikal(p);
////
//
//            long timeEnd = System.nanoTime();
//            System.out.println("Count: " + count);
//            System.out.println("Time: " + (timeEnd - timeStart) * 1.0 / 1000000000 + " sec");
//            db.close();
//
////
////            } catch (IOException e) {
////                e.printStackTrace();
////
////            }
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
public void ucitaj_artikle(){

    ConnectivityManager wifi = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info=wifi.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if (info.isConnected()) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.ucitavanje))
                .setMessage(getResources().getString(R.string.dodajBazu))
                .setPositiveButton(getResources().getString(R.string.da), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
                        final String accessToken = prefs.getString("access-token", null);
                        File exportDir = new File(Environment.getExternalStorageDirectory(), "updatedata");
                        if (!exportDir.exists()) {
                            exportDir.mkdirs();
                        }
                        new DownloadTask(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"), getApplicationContext()).execute();


//                                    DatabaseHelper dh = new DatabaseHelper(HPActivity.this);
//                                    dh.clearDatabase(HPActivity.this);
//                                    ReadDatabase(HPActivity.this);
//                                    Toast.makeText(HPActivity.this, "Uspjesno uradjeno", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.Ne), null)
                .show();
    }
    else{
        Toast.makeText(MainActivity.this, "Morate ukljuciti WIFI", Toast.LENGTH_SHORT).show();
    }
}
// Adapter for the viewpager using FragmentPagerAdapter

    private class InsertDataTask extends AsyncTask<ArrayList<Product>,Void,Void> {
        final ProgressDialog dialog = new ProgressDialog(
                MainActivity.this);
        DatabaseHelper dh=new DatabaseHelper(MainActivity.this);


        // can use UI thread here
        protected void onPreExecute() {

            this.dialog.setMessage("Ucitavanje baze...");

            this.dialog.show();
            dh.clearDatabase(MainActivity.this);

        }

        // automatically done on worker thread (separate from UI thread)
        protected Void doInBackground(final ArrayList<Product>... args) {
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());
            db.InsertArtikal(args[0]);
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            // reset the output view by retrieving the new data
            // (note, this is a naive example, in the real world it might make sense
            // to have a cache of the data and just append to what is already there, or such
            // in order to cut down on expensive database operations)

        }
    }
    public class DownloadTask extends AsyncTask {
        private DbxClientV2 dbxClient;
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private Context context;

        DownloadTask(DbxClientV2 dbxClient, Context context) {
            this.dbxClient = dbxClient;
            this.context = context;
        }
        protected void onPreExecute() {

            this.dialog.setMessage("Dohvacanje baze...");
            this.dialog.show();

        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Object doInBackground(Object[] params) {


            // Download the 1st file in the Apps Folder
            File path = new File(Environment.getExternalStorageDirectory().toString() + "/updatedata/");
            FileMetadata metadata = null;
            try {
                metadata = (FileMetadata) dbxClient.files().listFolder("/Artikli").getEntries().get(0);
//            metadata = (FileMetadata) dbxClient.files().getMetadata("https://www.dropbox.com/s/cm8tn3kshbrwnwx/Male%20soralne%20elektrane.pptx?dl=0");
            } catch (DbxException e) {
                e.printStackTrace();
            }
            File file = new File(path, metadata.getName());

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    throw new RuntimeException("Unable to create directory: " + path);
                }
            } else if (!path.isDirectory()) {
                throw new IllegalStateException("Download path is not a directory: " + path);
            }

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                dbxClient.files().download(metadata.getPathLower(), metadata.getRev())
                        .download(outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DownloadErrorException e) {
                e.printStackTrace();
            } catch (DbxException e) {
                e.printStackTrace();
            }

            Log.i("Check : ", "File Downloaded");

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Toast.makeText(context, "File download successfully", Toast.LENGTH_SHORT).show();
            DatabaseHelper dh = new DatabaseHelper(MainActivity.this);
            dh.clearDatabase(MainActivity.this);
            ReadDatabase(MainActivity.this);
            Toast.makeText(MainActivity.this, "Uspjesno uradjeno", Toast.LENGTH_SHORT).show();

        }


    }
}
