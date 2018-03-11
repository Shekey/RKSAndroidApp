package com.example.ajdin.probafragmenti.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


import com.example.ajdin.probafragmenti.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Created by ajdin on 10/19/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public SQLiteDatabase dbb;
    public final String TAG="DATABASE HELPER";
    public static final String Table_name = "Artikal";
    public static final String Bar_kod = "bar_kod";
    public static final String Cijena = "cijena";
    public static final String ID = "id";
    public static final String Naziv_artikla = "naziv";
    public static final String Zaliha = "zaliha";
    public static final String Jedinica_mjere = "jedinica_mjere";
    public static final String Database_name = "NUR.db";
    public static final int Database_version = 1;

    // racun create
    public static String Table_Name_racun = "Racun";
    public static String RACUN_ID = "racun_id";
    public static String Datum_Izdavanja = "racun_datum";
    public static String Kupac = "racun_kupac";
    public static String Iznos_racuna = "iznos_racuna";


    ////


    public DatabaseHelper(Context context) {
        super(context, Database_name, null, Database_version);
         dbb = this.getWritableDatabase();

    }
//
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA synchronous = 'OFF'");
        db.execSQL("PRAGMA temp_store = 'MEMORY'");
        db.execSQL("PRAGMA cache_size = '500000'");
        db.execSQL("PRAGMA encoding='UTF-16'");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA encoding='UTF-16'");
        db.execSQL("CREATE TABLE `Artikli` (\n " +
        "\t`Artikal_id`\t INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`Naziv`\tTEXT NOT NULL DEFAULT '---',\n" +
                "\t`JM`\tTEXT NOT NULL DEFAULT '---',\n" +
                "\t`Cijena`\tTEXT NOT NULL DEFAULT 0,\n" +
                "\t`Bar_kod`\tTEXT NOT NULL, \n" +
                "\t`Stanje`\tTEXT  DEFAULT 0\n" +
                ");");



       // db.execSQL("CREATE TABLE `Racun` (\n" +
         //       "\t`Racun_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
         //       "\t`NazivArtikla`\tTEXT NOT NULL,\n" +
          //      "\t`Cijena`\tTEXT NOT NULL,\n" +
           //     "\t`Kolicina`\tTEXT NOT NULL,\n" +
           //     "\t`Kupac_ime`\tTEXT NOT NULL\n" +
//                ");");

    }
    public void clearDatabase(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
       database.execSQL("drop table Artikli");
        database.execSQL("PRAGMA encoding='UTF-16'");
        database.execSQL("CREATE TABLE `Artikli` (\n " +
                "\t`Artikal_id`\t INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`Naziv`\tTEXT NOT NULL DEFAULT '---',\n" +
                "\t`JM`\tTEXT NOT NULL DEFAULT '---',\n" +
                "\t`Cijena`\tTEXT NOT NULL DEFAULT 0,\n" +
                "\t`Bar_kod`\tTEXT NOT NULL, \n" +
                "\t`Stanje`\tTEXT  DEFAULT 0\n" +
                ");");

        database.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete database " + Database_name);
        onCreate(db);
    }



    public void ReadFile() throws IOException {


        String csvFile = Environment.getExternalStorageDirectory().toString()+"/racuni/art.txt";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        ArrayList<Product> products=new ArrayList<Product>();
        Product product;

        try
        {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null)
            {
                // use comma as separator
                String[] lista = line.split(cvsSplitBy);
                String cijena=lista[3];
               cijena= cijena.replace(",",".");
                InsertArtikal(lista[4],lista[2],lista[1],cijena,null);

            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                    InsertArtikal(products);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
    }


    public void UbaciArtikal(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();



            ContentValues contentValues = new ContentValues();
            contentValues.put("Bar_kod", p.getBar_kod());
            contentValues.put("JM", "---");
            contentValues.put("Naziv", "---");
            contentValues.put("Cijena", p.getPrice().toString());
            contentValues.put("Stanje", "---");
            long result= db.insert("Artikli",null,contentValues);

            Log.d(TAG,"Inserted "+result);
        }








    public void InsertArtikal(ArrayList<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();


        for (Product p:products) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Bar_kod", p.getBar_kod());
            contentValues.put("JM", p.getJedinica_mjere());
            contentValues.put("Naziv", p.getName());
            contentValues.put("Cijena", p.getPrice().toString());
            contentValues.put("Stanje", p.getStanje());
            long result= db.insert("Artikli",null,contentValues);

            Log.d(TAG,"Inserted "+result);
        }





    }


    public boolean InsertArtikal(String bar_kod, String jedinica_mjere, String naziv_artikla, String cijena, String zaliha) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("bar_kod", bar_kod);
        contentValues.put("JM", jedinica_mjere);
        contentValues.put("Naziv", naziv_artikla);
        contentValues.put("Cijena", cijena);
        contentValues.put("Stanje", zaliha);
        long result= db.insertWithOnConflict("Artikli", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
      //  long result = db.insert("Artikli", null, contentValues);

        if (result == -1) {

            return false;
        } else {

            return true;
        }
    }
    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productList = db.rawQuery("select * from Artikli", null);
        productList.moveToFirst();
        productList.getCount();
        productList.close();
        db.close();
        return productList;
    }

     public Product getData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productList = db.rawQuery("select * from Artikli  where Bar_kod='" + id + "'", null);
        if (productList.getCount()==0){
            Product product=null;
            return product;
        }
        productList.moveToFirst();
        BigDecimal decimal= BigDecimal.valueOf(Double.valueOf(productList.getString(3)));
        Product product=new Product(productList.getString(0),productList.getString(1),decimal,productList.getString(4),productList.getString(2),productList.getString(5));
        db.close();
        return product;

    }
    public String getDataString(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productList = db.rawQuery("select * from Artikli  where Bar_kod='" + id + "'", null);
        if (productList.getCount()==0){

            return null;
        }
        productList.moveToFirst();
        BigDecimal decimal= BigDecimal.valueOf(Double.valueOf(productList.getString(3)));
        Product product=new Product(productList.getString(0),productList.getString(1),decimal,productList.getString(4),productList.getString(2),productList.getString(5));
        db.close();
        return product.getName();

    }



    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from Artikli " , null);
        return res;

    }
    public void smanjiKolicinu(String bk,String kolicina){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE Artikli SET Stanje=Stanje-"+kolicina+" WHERE bar_kod='"+bk+"';");

    }

    public boolean checkKolicina(String kolicina,String Bar,double kol){
        SQLiteDatabase db = this.getWritableDatabase();
        Double value,value2;
        double kolicinaget=kol;
        double sum;
        Cursor productList = db.rawQuery("select * from Artikli  where Bar_kod='" + Bar + "'", null);
        if (productList != null ) {
            if (productList.moveToFirst()){
                String Stanje = productList.getString(productList.getColumnIndex("Stanje"));
                value=Double.valueOf(Stanje);
                value2=Double.valueOf(kolicina);
                sum=value-value2-kol;
               if (sum>=0.0){
                   return true;

               }
               else {
                   return false;
               }
            }
        }

            return false;

    }


     public void showMessage(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }



}
