package com.example.ajdin.probafragmenti.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.example.ajdin.probafragmenti.MainActivity;
import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.adapter.Cart;
import com.example.ajdin.probafragmenti.adapter.CartHelper;
import com.example.ajdin.probafragmenti.database.DatabaseHelper;
import com.example.ajdin.probafragmenti.fragments.FragmentOneOne;
import com.example.ajdin.probafragmenti.model.Product;
import com.opencsv.CSVReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NameFragment extends Fragment {

    Button btn,btn2;
    EditText txt;
    TextView txtView;
    ListView lstView;
    private List<Metadata> lv_arr;
    public ArrayList<String> files;
    public ArrayList<String> Kfiles;
    public static final long NOTIFY_INTERVAL = 10 * 3000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    private ArrayAdapter<String> arrayAdapter;
    //    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name,container,false);

        btn=(Button)view.findViewById(R.id.Confirm_button);

        txt=(EditText)view.findViewById(R.id.imeInput);

        lstView=(ListView)view.findViewById(R.id.list_history);
        lstView.setEmptyView(view.findViewById(R.id.emptyElement2));
        txt.requestFocus();
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        // schedule task

//        if (!tokenExists()){
//
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//
//            startActivity(intent);
//
//        }

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
//            lv_arr = getListFile("/Racuni/");
//            List<String> strings=new ArrayList<String>();
//            for (Metadata m:lv_arr) {
//                strings.add(m.getPathDisplay());
//
//            }
            Kfiles=getList();
            files=new ArrayList<>();

//            new GetList(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"), getActivity()).execute();


            //lv_arr = (String[]) listOfStrings.toArray();
            arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, files) {

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    text1.setPadding(16, 0, 16, 0);
                    text2.setPadding(16, 0, 16, 0);
                    text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    text1.setAllCaps(true);
                    text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    text2.setTextColor(Color.parseColor("#867e7e"));
                    String ime[] = files.get(position).split("[0-9_-]");
                    String datum[] = files.get(position).split("[a-zA-Z-]");

                    text2.setText(datum[datum.length - 1]);
                    text1.setText(ime[0]);
                    return view;


                }
            };

            lstView.setAdapter(arrayAdapter);
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(txt.getText().toString())) {
                    txt.setError("Unesite ime kupca");
                    return;
                } else {

                 MainAcitivityFragment fragment = new MainAcitivityFragment();
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("podaci",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("ime",txt.getText().toString());
                    editor.commit();

                    ft.replace(R.id.container_name, fragment);
                    ft.commit();
                    return;

                }
            }
        });
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public static final String TAG = "NAME ACTIVITY";

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (id < 0) {
                    Log.d(TAG, "In MainActivity clicked header");
                    return;
                }
                String path = files.get(position);
                Read(path);

                //TREBA OVDJE URADITI
                MainAcitivityFragment fragment = new MainAcitivityFragment();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("podaci",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("path",path);
                editor.commit();
                ft.replace(R.id.container_name, fragment);
                ft.commit();
                //DO OVDJE

//                SharedPreferences.Editor spreferencesEditor = sharedPreferences.edit();
//                spreferencesEditor.putString("path",path);
//                spreferencesEditor.commit();



                //Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                // Bundle bundle = new Bundle();
                // bundle.putSerializable("product", product);
                // Log.d(TAG, "View product: " + product.getName());
                // intent.putExtras(bundle);
                // startActivity(intent);
            }
        });


        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    if (TextUtils.isEmpty(txt.getText().toString())) {
                        txt.setError("Unesite ime kupca");
                        return false;
                    } else {
//                        SharedPreferences.Editor spreferencesEditor = sharedPreferences.edit();
//                        spreferencesEditor.putString("ime",txt.getText().toString());
//                        spreferencesEditor.commit();
                        MainAcitivityFragment fragment = new MainAcitivityFragment();
                        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        getActivity().getSupportFragmentManager().popBackStack();
                        ft.replace(R.id.container_name, fragment);
                        return true;
                        //TREBA OVDJE URADITI

                    }


                }
                return true;
            }
        });


        return view;
    }

    private boolean tokenExists() {
        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }
    public List<Metadata> getListFile(String path) {

        DbxRequestConfig config = new DbxRequestConfig("dropbox/AjdinovProjekt", "en_US");
        DbxClientV2 client = new DbxClientV2(config, "aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV");
        if (client == null) {
            return null;
        }

        try {
            return client.files().listFolder(path).getEntries();
        } catch (DbxException e) {
            return null;
        }

    }

public String [] getLv_arr() throws DropboxException {
    String[] fnames = null;
    int i=0;
    AppKeyPair appKeys = new AppKeyPair("h01r2htv5wd572y", "48g39vbfomaym0i");
    AndroidAuthSession session = new AndroidAuthSession(appKeys);
    DropboxAPI<AndroidAuthSession> mApi = new DropboxAPI<>(session);
    DropboxAPI.Entry dirent = mApi.metadata("/Racuni/", 1000, null, true, null);
    ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
    ArrayList<String> dir=new ArrayList<String>();
    for (DropboxAPI.Entry ent: dirent.contents)
    {
        files.add(ent);// Add it to the list of thumbs we can choose from
        //dir = new ArrayList<String>();
        dir.add(new String(files.get(i++).path));
    }
    fnames=dir.toArray(new String[dir.size()]);

    return fnames;
}
    private ArrayList<String> getList() {

        ArrayList<String> inFiles = new ArrayList<String>();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "racuni");
        File exportDir2 = new File(Environment.getExternalStorageDirectory(), "racunidevice");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        if (!exportDir2.exists())
        {
            exportDir2.mkdirs();
        }
        String path = Environment.getExternalStorageDirectory().toString()+"/racunidevice";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        String[] files = directory.list();
        Log.d("Files", "Size: "+ files.length);
        int count=files.length-1;
        while (count>0||count==0){
            if (files[count].equals("Artikli.txt") || files[count].equals("desktop.ini")){
                if (count == 0) {
                    break;
                }
                --count;
            }
            inFiles.add(files[count--]);

        }

        return inFiles;
    }






    public void Read(String path){
        Product products;

        try {
            // BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("myFile.csv"));
            CSVReader csvReader = new CSVReader(new InputStreamReader
                    (new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/racunidevice/"+path), 8192 * 32)),';');
            try {
                DatabaseHelper db=new DatabaseHelper(getActivity());
                int count = 0;
                String[] line;
                long timeStart = System.nanoTime();
                while((line = csvReader.readNext()) != null) {
                    if (line.length == 3) {
                        count++;
                        Product temp = db.getData(line[0]);
                        if (temp != null) {
                            BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(line[2]));
                            products = new Product(temp.getId(), temp.getName(), decimal, temp.getBar_kod(), temp.getJedinica_mjere(), temp.getStanje());
                            Cart cart = CartHelper.getCart();
                            cart.add(products, Double.valueOf(line[1]), line[2]);


                        }
                        if (count >= 150000) {
                            break;
                        }
                    } else{
                        count++;
                        Product temp = db.getData(line[0]);
                        if (temp != null) {
                            BigDecimal decimal = temp.getpPrice();
                            products = new Product(temp.getId(), temp.getName(), decimal, temp.getBar_kod(), temp.getJedinica_mjere(), temp.getStanje());
                            Cart cart = CartHelper.getCart();
                            cart.add(products, Double.valueOf(line[1]), decimal.toString());


                        }
                        if (count >= 150000) {
                            break;
                        }

                    }
                }

                long timeEnd = System.nanoTime();
                System.out.println("Count: " + count);
                System.out.println("Time: " + (timeEnd - timeStart) * 1.0 / 1000000000 + " sec");



            } catch (IOException e) {
                e.printStackTrace();

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
    public void ReadFile(String path) throws IOException {
//       DatabaseHelper db=new DatabaseHelper(Name.this);
//       String csvFile = Environment.getExternalStorageDirectory().toString()+"/racuni/"+path;
//       BufferedReader br = null;
//       String line = "";
//       String cvsSplitBy = ";";
//      Product products;
//       try {
//           br = new BufferedReader(new FileReader(csvFile));
//           while ((line = br.readLine()) != null) {
//               // use comma as separator
//               String[] lista = line.split(cvsSplitBy);
//               Product temp = db.getData(lista[0]);
//               if (temp != null) {
//                   BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(lista[1]));
//                   products = new Product(temp.getId(), temp.getName(), decimal, temp.getBar_kod(), temp.getJedinica_mjere(), temp.getStanje());
//                   Cart cart = CartHelper.getCart();
//                   cart.add(products, Double.valueOf(lista[2]), lista[1]);
//
//               }
//
//           }
//       }
//       catch (FileNotFoundException e)
//       {
//           e.printStackTrace();
//       }
//       catch (IOException e)
//       {
//           e.printStackTrace();
//       }
//       finally
//       {
//           if (br != null)
//           {
//               try
//               {
//                   br.close();
//                   Intent intent=new Intent(Name.this,MainActivity.class);
//                   startActivity(intent);
//
//               }
//               catch (IOException e)
//               {
//                   e.printStackTrace();
//               }
//           }
//       }
//   }
//
        DatabaseHelper db=new DatabaseHelper(getActivity());
        Product products;
        String csvFile =Environment.getExternalStorageDirectory().toString()+"/racuni/"+ path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        StringBuffer stringBuffer = new StringBuffer();
        try {


            br = new BufferedReader(new FileReader(csvFile));
            stringBuffer.append("BAR KOD   KOLICINA  CIJENA   "+ "\n \n");
            while ((line = br.readLine()) != null) {
                if (line==""){
                    break;
                }
                // use comma as separator
                String[] lista = line.split(cvsSplitBy);
                Product temp=db.getData(lista[0]);
                if (temp!=null) {
                    BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(lista[1]));
                    products = new Product(temp.getId(), temp.getName(), decimal, temp.getBar_kod(), temp.getJedinica_mjere(), temp.getStanje());
                    Cart cart = CartHelper.getCart();
                    cart.add(products, Double.valueOf(lista[2]), lista[1]);
                }
                else {
                    Toast.makeText(getActivity(), "Nazalost neuspjesno", Toast.LENGTH_SHORT).show();
                }

            }



            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra("path",path);
            startActivity(intent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    class GetList extends AsyncTask {

        private DbxClientV2 dbxClient;

        private Context context;
        private ArrayList<String> lista;
        private List<Metadata> meta;

        public GetList(DbxClientV2 dbxClient, Context context) {
            this.dbxClient = dbxClient;
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                meta = dbxClient.files().listFolder("/Racuni").getEntries();


                Log.d("Upload Status", "Success");
            } catch (DbxException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            lista=new ArrayList<>();
            for (Metadata m:meta) {
                lista.add(m.getName());
            }
            Kfiles=getListD(lista);
            files.clear();
            for (String s : Kfiles){
                arrayAdapter.add(s);
            }

            arrayAdapter.notifyDataSetChanged();





        }
    }
    private ArrayList<String> getListD(ArrayList<String> lusi) {

        ArrayList<String> inFiles = new ArrayList<String>();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "racuni");
        File exportDir2 = new File(Environment.getExternalStorageDirectory(), "racunidevice");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        if (!exportDir2.exists())
        {
            exportDir2.mkdirs();
        }
        String path = Environment.getExternalStorageDirectory().toString()+"/racunidevice";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        String[] files = directory.list();
        Log.d("Files", "Size: "+ files.length);
        int count=files.length-1;
        while (count>0||count==0){
            if (files[count].equals("Artikli.txt") || files[count].equals("desktop.ini")){
                if (count == 0) {
                    break;
                }
                --count;
            }
            inFiles.add(files[count--]);

        }
        ArrayList<String> kopija=new ArrayList<>();

        for (String d:inFiles){
            for (String dd:lusi){
                if (d.equals(dd)){
                    kopija.add(dd);

                }
            }

        }

        return kopija;
    }
    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
//                    ConnectivityManager wifi = (ConnectivityManager)getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
//                    NetworkInfo info=wifi.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                    if (info.isConnected()) {
                        new GetList(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"), getActivity()).execute();
//                    }
//                    else {
//                        Toast.makeText(getContext(), "Nemate interneta", Toast.LENGTH_SHORT).show();
//                    }

                }
                // display toast


            });
        }


    }




}



