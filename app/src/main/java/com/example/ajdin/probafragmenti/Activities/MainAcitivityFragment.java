package com.example.ajdin.probafragmenti.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ajdin.probafragmenti.MainActivity;
import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.adapter.Cart;
import com.example.ajdin.probafragmenti.adapter.CartHelper;
import com.example.ajdin.probafragmenti.adapter.CartItemAdapter;
import com.example.ajdin.probafragmenti.adapter.Saleable;
import com.example.ajdin.probafragmenti.constant.Constant;
import com.example.ajdin.probafragmenti.database.DatabaseHelper;
import com.example.ajdin.probafragmenti.model.CartItem;
import com.example.ajdin.probafragmenti.model.Product;


import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.Map;

public class MainAcitivityFragment extends Fragment {
    private static final String TAG = "MainActivity";
    DatabaseHelper helper;
    private Cursor productList;
    Button barK, bZavrsi,blear,bZaduzenje;
   public EditText input;
    TextView tvTotalPrice;
    String path,ime;
    String Acces_token;
    SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main2, container, false);
        helper= new DatabaseHelper(getActivity());
        barK=(Button)view.findViewById(R.id.Ok_button);
        bZavrsi=(Button)view.findViewById(R.id.zavrsi);
        blear=(Button)view.findViewById(R.id.clearAll);
        input=(EditText)view.findViewById(R.id.barKodInput);
        input.requestFocus();
        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        Acces_token = prefs.getString("access-token", null);


//        SpannableString content = new SpannableString(getText(R.string.shopping_cart));
//              content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvTotalPrice=(TextView)view.findViewById(R.id.tvTotalPrice);
        ((MainActivity) getActivity())
                .setActionBarTitle("Korpa artikala");

        sharedPreferences=getActivity().getSharedPreferences("podaci", Context.MODE_PRIVATE);
        ime=sharedPreferences.getString("ime","");
        path=sharedPreferences.getString("path","");




        input.requestFocus();
        productList= helper.getAllData();

        int count=0;
        for(productList.moveToFirst(); !productList.isAfterLast(); productList.moveToNext()) {
            count++;
            // BigDecimal decimal=BigDecimal.valueOf(Double.valueOf(productList.getString(3)));
            // Product product=new Product(productList.getInt(0),productList.getString(1),decimal,productList.getString(4),productList.getString(2),null);
            //Constant.PRODUCT_LIST.add(product);
        }



        ListView lvProducts = (ListView)view.findViewById(R.id.lvProducts);
      //  lvProducts.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.cart_header, lvProducts, false));
        //

        final Cart cart = CartHelper.getCart();
        final CartItemAdapter cartItemAdapter = new CartItemAdapter(getActivity());

        cartItemAdapter.updateCartItems(getCartItems(cart));
        tvTotalPrice.setText(String.valueOf(cart.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));


        lvProducts.setAdapter(cartItemAdapter);

        //



        blear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.zavrsetak_svega))
                        .setMessage(getResources().getString(R.string.cisti_sve))
                        .setPositiveButton(getResources().getString(R.string.da), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor spreferencesEditor = sharedPreferences.edit();
                                spreferencesEditor.clear();
                                spreferencesEditor.commit();
                                cart.clear();
                                cartItemAdapter.updateCartItems(getCartItems(cart));
                                cartItemAdapter.notifyDataSetChanged();
                                tvTotalPrice.setText(String.valueOf(cart.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.Ne), null)
                        .show();





//                SharedPreferences shared=getSharedPreferences("path",Context.MODE_PRIVATE);
//                shared.edit().clear();
//                shared.edit().commit();


            }
        });

        bZavrsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItemAdapter.getCount()==0){
                    Toast.makeText(getActivity(), "Nazalost, niste nista unijeli", Toast.LENGTH_SHORT).show();
                    return;
                }

                ConnectivityManager wifi = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=wifi.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (info.isConnected()) {
                    if (sharedPreferences.getString("ime", "") != "" && sharedPreferences.getString("ime", "") != null) {
                        String zadropBox = exportDB(getCartItems(cart), cartItemAdapter.getCount(), sharedPreferences.getString("ime", ""));
                        SharedPreferences.Editor spreferencesEditor = sharedPreferences.edit();
                        spreferencesEditor.clear();
                        spreferencesEditor.commit();
                        Toast.makeText(getActivity(), "Uspjesno kreiran racun", Toast.LENGTH_SHORT).show();
                        cart.clear();
                        cartItemAdapter.updateCartItems(getCartItems(cart));
                        cartItemAdapter.notifyDataSetChanged();
                        tvTotalPrice.setText(String.valueOf(cart.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));
//                    Intent intent = new Intent(getApplicationContext(), DropBox.class);
//                    intent.putExtra("pathFile",zadropBox);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                        File file = new File(zadropBox);
                        new UploadTask(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"), file, getActivity().getApplicationContext()).execute();
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {


                        exportDBold(getCartItems(cart), cartItemAdapter.getCount(), sharedPreferences.getString("path", ""));


                        Toast.makeText(getActivity(), "Uspjesno kreiran racun", Toast.LENGTH_SHORT).show();
                        cart.clear();
                        cartItemAdapter.updateCartItems(getCartItems(cart));
                        cartItemAdapter.notifyDataSetChanged();
                        tvTotalPrice.setText(String.valueOf(cart.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("pathFile",Environment.getExternalStorageDirectory().toString()+"/racunidevice/"+sharedPreferences.getString("path",""));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                        String putanja = Environment.getExternalStorageDirectory().toString() + "/racunidevice/" + sharedPreferences.getString("path", "");
                        File file = new File(putanja);
                        new UploadTask(DropboxClient.getClient("aLRppJLoiTAAAAAAAAAABt0hedpD0SdE5AcEPMR5neXz-_zF09coKzJlZmuMq_FV"), file,getActivity(). getApplicationContext()).execute();
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        return;


                    }
                }
                else {
                    Toast.makeText(getActivity(), "Morate ukljuciti WIFI", Toast.LENGTH_SHORT).show();

                }
                //  helper.InsertIntoRacun(cartItemAdapter, cartItemAdapter.getCount());




//                SharedPreferences shared=getSharedPreferences("path",Context.MODE_PRIVATE);
//                shared.edit().clear();
//                shared.edit().commit();




            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    Product product = helper.getData(input.getText().toString());
                    if (product==null){
                        Toast.makeText(getActivity(), "Nismo pronasli taj artikal", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product", product);
                    ProductFragment fragment = new ProductFragment();
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    fragment.setArguments(bundle);
                    ft.replace(R.id.mainfragment, fragment).addToBackStack("productFrag");
                    ft.commit();

                    return true;
                }
                return false;
            }
        });

        //  lvProducts.setAdapter(productAdapter);

        barK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input.getText().toString())){
                    input.setError("Unesite bar kod");
                    input.setSelection(0);
                    return;
                }
                Product product = helper.getData(input.getText().toString());
                if (product==null){
                    Toast.makeText(getActivity(), "Nismo pronasli taj artikal", Toast.LENGTH_SHORT).show();
                    return;
//                    Toast.makeText(MainActivity.this, "Nismo pronasli taj artikal", Toast.LENGTH_SHORT).show();

                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                ProductFragment fragment = new ProductFragment();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.mainfragment, fragment).addToBackStack("productFrag");


                ft.commit();
            }
        });
        lvProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position<0) {
                    return false;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.delete_item))
                        .setMessage(getResources().getString(R.string.delete_item_message))
                        .setPositiveButton(getResources().getString(R.string.da), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<CartItem> cartItems = getCartItems(cart);

                                cart.remove(cartItems.get(position).getProduct());
                                cartItems.remove(position);
                                cartItemAdapter.updateCartItems(cartItems);
                                cartItemAdapter.notifyDataSetChanged();
                                tvTotalPrice.setText(String.valueOf(cart.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.Ne), null)
                        .show();
                return false;
            }
        });


return view;
    }



    private void exportDBold(List<CartItem> cartItems, int count, String pathfile) {

        File dbFile=getActivity().getDatabasePath("NUR.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity().getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "racuni");
        File exportDir2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "racunidevice");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        if (!exportDir2.exists())
        {
            exportDir2.mkdirs();
        }


        File file = new File(exportDir,pathfile);

        if (file.delete()){
            Toast.makeText(getActivity(),"izbrisan fajl", Toast.LENGTH_SHORT);
        }
        File file2 = new File(exportDir,pathfile);
        File file3 = new File(exportDir2,pathfile);

        try
        {
            file2.createNewFile();
            file3.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file2));
            CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file3));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            // Cursor curCSV = db.rawQuery("SELECT * FROM Artikli",null);

            for (int i=0;i<count;i++){

                String arrStr[] ={cartItems.get(i).getProduct().getBar_kod(), String.valueOf(cartItems.get(i).getQuantity()), String.valueOf(cartItems.get(i).getProduct().getPrice())};

                csvWrite.writeNext(arrStr);
                helper.smanjiKolicinu(cartItems.get(i).getProduct().getBar_kod(),String.valueOf(cartItems.get(i).getQuantity()));
                csvWrite2.writeNext(arrStr);

            }

            csvWrite.close();
            csvWrite2.close();

        }
        catch(Exception sqlEx)
        {
            sqlEx.getStackTrace();
        }


    }

    private List<CartItem> getCartItems(Cart cart) {
        List<CartItem> cartItems = new ArrayList<CartItem>();


        Map<Saleable, Double> itemMap = cart.getItemWithQuantity();


        for (Map.Entry<Saleable, Double> entry : itemMap.entrySet()) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct((Product) entry.getKey());
            cartItem.setQuantity(entry.getValue());
            cartItems.add(cartItem);
        }

//        Map<Saleable, Double> map = new TreeMap<Saleable, Double>(
//                new Comparator<Saleable>() {
//
//                    @Override
//                    public int compare(Saleable o1, Saleable o2) {
//                        return o2.getName().compareTo(o2.getName());
//                    }
//
//                });
//        map.putAll(itemMap);

//
//        System.out.println("After Sorting:");
//        Set set2 = map.entrySet();
//        Iterator iterator2 = set2.iterator();
//        while(iterator2.hasNext()) {
//            Map.Entry me2 = (Map.Entry)iterator2.next();
//            CartItem cartItem = new CartItem();
//            cartItem.setProduct((Product) me2.getKey());
//            cartItem.setQuantity((Double) me2.getKey());
//            cartItems.add(cartItem);
//        }


        // Log.d(TAG, "Cart item list: " + cartItems);
        return cartItems;
    }
    private String exportDB(List<CartItem> items, int size, String imep) {

        File dbFile=getActivity().getDatabasePath("NUR.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity().getApplicationContext());
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

        String timeStamp = new SimpleDateFormat("dd MM yyyy HH:mm").format(Calendar.getInstance().getTime());

        File file = new File(exportDir,imep+"---"+timeStamp+".txt");
        File file2 = new File(exportDir2,imep+"---"+timeStamp+".txt");

        try
        {
            file.createNewFile();
            file2.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file2));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM Artikli",null);

            for (int i=0;i<size;i++){

                String arrStr[] ={items.get(i).getProduct().getBar_kod(), String.valueOf(items.get(i).getQuantity()), String.valueOf(items.get(i).getProduct().getPrice())};
                csvWrite.writeNext(arrStr);
                helper.smanjiKolicinu(items.get(i).getProduct().getBar_kod(),String.valueOf(items.get(i).getQuantity()));
                csvWrite2.writeNext(arrStr);


            }

            csvWrite.close();
            csvWrite2.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
            // Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
        return Environment.getExternalStorageDirectory().toString()+ "/racunidevice/"+
                imep+"---"+timeStamp+".txt";

    }

    private String ZaduzenjePrint(List<CartItem> items, int size, String imep) {

        File dbFile=getActivity().getDatabasePath("NUR.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity().getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "racunidevice");

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmm").format(Calendar.getInstance().getTime());

        File file = new File(exportDir,imep+"---"+timeStamp+".txt");


        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM Artikli",null);

            for (int i=0;i<size;i++){
//                int brojac=i+1;
//                String  brojac_string=String.valueOf(brojac);
                String arrStr[] ={items.get(i).getProduct().getBar_kod(), String.valueOf(items.get(i).getQuantity())};
                csvWrite.writeNext(arrStr);



            }

            csvWrite.close();

            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
            // Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
        return Environment.getExternalStorageDirectory().toString()+ "/racunidevice/"+
                imep+"---"+timeStamp+".txt";

    }


    private String ZaduzenjePrintOld(List<CartItem> cartItems, int count, String pathfile) {

        File dbFile=getActivity().getDatabasePath("NUR.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity().getApplicationContext());

        File exportDir2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "racunidevice");

        if (!exportDir2.exists())
        {
            exportDir2.mkdirs();
        }



        File file3 = new File(exportDir2,pathfile);

        try
        {

            file3.createNewFile();

            CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file3));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            // Cursor curCSV = db.rawQuery("SELECT * FROM Artikli",null);

            for (int i=0;i<count;i++){
                String arrStr[] ={cartItems.get(i).getProduct().getBar_kod(), String.valueOf(cartItems.get(i).getQuantity())};
                csvWrite2.writeNext(arrStr);

            }


            csvWrite2.close();

        }
        catch(Exception sqlEx)
        {
            sqlEx.getStackTrace();
        }

  return Environment.getExternalStorageDirectory().toString()+ "/racunidevice/"+ pathfile;
    }



}
