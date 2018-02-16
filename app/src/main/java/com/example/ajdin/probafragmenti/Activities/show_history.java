package com.example.ajdin.probafragmenti.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ajdin.probafragmenti.MainActivity;
import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.adapter.Cart;
import com.example.ajdin.probafragmenti.adapter.CartHelper;
import com.example.ajdin.probafragmenti.database.DatabaseHelper;
import com.example.ajdin.probafragmenti.model.Product;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;

public class show_history extends Fragment {

    ListView lstView;
    private String[] lv_arr = {};
    ArrayList<String> files;
    Button nazad,brisiSve;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_history, container, false);

        lstView=(ListView)view.findViewById(R.id.list_history1);
        lstView.setEmptyView(view.findViewById(R.id.emptyElement));
        ((MainActivity) getActivity())
                .setActionBarTitle("Pocetna");
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

            files = getList();
            //lv_arr = (String[]) listOfStrings.toArray();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, files) {

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    text1.setPadding(16, 0, 16, 0);
                    text2.setPadding(16, 0, 16, 0);
                    text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    text2.setTextColor(Color.parseColor("#867e7e"));
                    ;
                    String ime[] = files.get(position).split("[0-9_-]");
                    String datum[] = files.get(position).split("[a-zA-Z-]");

                    text1.setText(datum[datum.length - 1]);
                    String suma = String.valueOf(izracunajTotal(files.get(position)));
                    text2.setText(suma + " KM");
                    return view;


                }
            };

            lstView.setAdapter(arrayAdapter);

            lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public static final String TAG = "NAME ACTIVITY";

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (id < 0) {
                        // Log.d(TAG, "In showHistory clicked header");
                        return;
                    }
                    String path = files.get(position);

                    try {
                        ReadFile(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            });
        }






        return view;
    }
    private boolean tokenExists() {
        SharedPreferences prefs =getActivity().getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }
public double izracunajTotal(String pathRacunanja){
    Product products;
    double suma=0.0;

    try {
        // BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("myFile.csv"));
        CSVReader csvReader = new CSVReader(new InputStreamReader
                (new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/racunidevice/"+pathRacunanja), 8192 * 32)),';');
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
                       suma+= Double.valueOf(line[2])*(Double.valueOf(line[1]));

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
return suma;

}

    public void  write(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "example.txt");
        DatabaseHelper dbhelper=new DatabaseHelper(getActivity());

//Write to file
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String[] lista={"Selam","Alejk"};
            csvWrite.writeNext(lista);

            csvWrite.close();

        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
            // Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

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
    private ArrayList<String> deleteList() {

        ArrayList<String> inFiles = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory().toString()+"/racunidevice";
        // Log.d("Files", "Path: " + path);
        File directory = new File(path);

        String[] files = directory.list();
        //  Log.d("Files", "Size: "+ files.length);
        int count=files.length-1;
        while (count>0||count==0){
            if (files[count].equals("Artikli.txt") || files[count].equals("desktop.ini")){
                if (count==0){
                    break;
                }
                --count;

            }
            inFiles.add(files[count]);
            String putanja=path+"/"+files[count];
            File deletedFile=new File(putanja);
            deletedFile.delete();
            --count;
        }

        return inFiles;
    }
   public void ReadFile(String path) throws FileNotFoundException {
      // File yourFile = new File(Environment.getExternalStorageDirectory()+"/racuni/"+path);

       String csvFile = Environment.getExternalStorageDirectory().toString()+"/racunidevice/"+path;
       BufferedReader br = null;
       String line = "";

       String cvsSplitBy = ";";

       try {
        StringBuffer buffer=new StringBuffer();
           br = new BufferedReader(new FileReader(csvFile));
           while ((line = br.readLine()) != null) {


               // use comma as separator
               String[] lista = line.split(cvsSplitBy);
               if (lista[0]==""){
                   break;
               }
               if (lista.length!=3){
                   break;
               }
               buffer.append(lista[0]+" \t \t "+ lista[2]+"\t  \t"+ lista[1]+"\n");


           }
           showMessage("Podaci",buffer.toString(),getActivity());

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



    public void showMessage(String title, String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }



    private void writeTextFile(File file, String text) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(text);
        writer.close();
    }

    private String readTextFile(File file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder text = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null)
        {
            text.append(line);
            text.append("\n");//BECAUSE ITS A LINE :D
        }
        reader.close();
        return text.toString();
    }

}
