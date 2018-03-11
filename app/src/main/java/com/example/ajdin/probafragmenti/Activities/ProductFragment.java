package com.example.ajdin.probafragmenti.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ajdin.probafragmenti.MainActivity;
import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.adapter.Cart;
import com.example.ajdin.probafragmenti.adapter.CartHelper;
import com.example.ajdin.probafragmenti.constant.Constant;
import com.example.ajdin.probafragmenti.database.DatabaseHelper;
import com.example.ajdin.probafragmenti.model.Product;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;


public class ProductFragment extends Fragment {
    private static final String TAG = "ProductActivity";

    TextView tvProductName;
    TextView tvProductPrice;
    TextView tvBarCode;
    TextView tvStanje;
    TextView tvJedinica_mjere;
    EditText new_price;
    EditText Kolicina;
    DatabaseHelper helper;

    Button bOrder;
    Product product;
    private Cart cart;
    private Double kol;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product, container, false);


        final Product data = (Product)getArguments().getSerializable("product");
        product=data;
        helper=new DatabaseHelper(getActivity());
        tvProductName = (TextView)view.findViewById(R.id.tvProductName);
        tvBarCode = (TextView) view.findViewById(R.id.tvBarCode);
        tvProductPrice = (TextView)view. findViewById(R.id.tvProductPrice);
        tvStanje= (TextView) view.findViewById(R.id.tvStanje);
        tvJedinica_mjere= (TextView)view. findViewById(R.id.tvJedinica_mjere);

        bOrder = (Button) view.findViewById(R.id.bOrder);
        new_price=(EditText)view.findViewById(R.id.new_price);
        Kolicina=(EditText)view.findViewById(R.id.quantity);
        Kolicina.clearFocus();
        ((MainActivity) getActivity())
                .setActionBarTitle("Detalji artikla");
        cart = CartHelper.getCart();
        kol = cart.getKolicinaUkupno(data);

        try {
            setProductProperties();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //Initialize quantity
        initializeQuantity();

        //On ordering of product
        bOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!Kolicina.getText().toString().isEmpty()) {
                   // Log.d(TAG, "Adding product: " + product.getName());
                   if (Double.valueOf(Kolicina.getText().toString()) > 0.0) {//unesena kolicina
                       if (new_price.getText().toString().trim().matches("")) {

                           if (helper.checkKolicina(Kolicina.getText().toString(), tvBarCode.getText().toString(), kol)) {//nema cijene
                               cart.add(product, Double.valueOf(Kolicina.getText().toString()), "");//cijena ""

                               MainAcitivityFragment fragment = new MainAcitivityFragment();
                               android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                               ft.replace(R.id.fragment_swap, fragment);
                               ft.commit();
                           } else {
                               Toast.makeText(getActivity(), "Nemamo na stanju ! ", Toast.LENGTH_SHORT).show();
                           }
                       } else {
//                        ^[0-9]\d*(\.\d+)?$
                           if (!new_price.getText().toString().matches("^[0-9]\\d*(\\.[1-9])?$")) {
                               new_price.setText("");
                               Toast.makeText(getActivity(), "Niste unijeli dobar format cijene,unosi se sa '.' ", Toast.LENGTH_SHORT).show();
                               return;
                           }
                           if (Double.valueOf(new_price.getText().toString()) > 0.0) {//unesena kolicina
                               if (helper.checkKolicina(Kolicina.getText().toString(), tvBarCode.getText().toString(), kol)) {
                                   product.setpPrice(BigDecimal.valueOf(Double.valueOf(new_price.getText().toString())));
                                   cart.add(product, Double.valueOf(Kolicina.getText().toString()), new_price.getText().toString());
                                   BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(new_price.getText().toString()));
                                   product.setpPrice(decimal);

                                   // ovdje ne moze ici Main
                                   MainAcitivityFragment fragment = new MainAcitivityFragment();
                                   android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                   ft.replace(R.id.fragment_swap, fragment);
                                   ft.commit();

                                   return;
                               } else {
                                   Toast.makeText(getActivity(), "Nemamo na stanju !", Toast.LENGTH_SHORT).show();
                               }
                           } else {
                               Toast.makeText(getActivity(), "Unesite cijenu vecu od 0.0", Toast.LENGTH_SHORT).show();
                           }

                       }
                   }
                   else {
                       Kolicina.setText("");
                       Toast.makeText(getActivity(), " Unesite kolicinu vecu od 0", Toast.LENGTH_SHORT).show();
                       Kolicina.requestFocus();
                   }
               }
               else {
                    Kolicina.setText("");
                    Toast.makeText(getActivity(), " Unesite kolicinu", Toast.LENGTH_SHORT).show();
                    Kolicina.requestFocus();
                    return;

//                    if (Kolicina.getText().toString().matches("^[1-9]\\d*(\\.\\d+)?$")) {
//                        if (!new_price.getText().toString().matches("")) {
//                            if (new_price.getText().toString().matches("^[1-9]\\d*(\\.\\d+)?$")) {
//                                BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(new_price.getText().toString()));
//                                product.setpPrice(decimal);
//                                cart.add(product, Double.valueOf(Kolicina.getText().toString()), new_price.getText().toString());
//
//                                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//                            else {
//                                Toast.makeText(ProductActivity.this, "Nije uredu cijena !", Toast.LENGTH_SHORT).show();
//                                new_price.setText("");
//                            }
//                        }
//                        else {
//
//                            cart.add(product, Double.valueOf(Kolicina.getText().toString()), "");
//
//                            Intent intent = new Intent(ProductActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                    else {

                }







            }
        });

return view;
    }



//    private void setShoppingCartLink() {
//        TextView tvViewShoppingCart = (TextView)findViewById(R.id.tvViewShoppingCart);
//        SpannableString content = new SpannableString(getText(R.string.shopping_cart));
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        tvViewShoppingCart.setText(content);
//        tvViewShoppingCart.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProductActivity.this, ShoppingCartActivity.class);
//
//                startActivity(intent);
//            }
//        });
//    }

    private void retrieveViews() {


    }

    private void setProductProperties() throws UnsupportedEncodingException {
        double stanjecalculated=Double.valueOf(product.getStanje())-kol;
        if (stanjecalculated<0.0){
            stanjecalculated=0.0;
        }
        tvProductName.setText(product.getName());
        tvProductPrice.setText(product.getPrice().toString());
        tvStanje.setText(String.valueOf(stanjecalculated));
        tvBarCode.setText(product.getBar_kod());
        tvJedinica_mjere.setText(product.getJedinica_mjere());
        Kolicina.requestFocus();


    }

    private void initializeQuantity() {
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, Constant.QUANTITY_LIST);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode== KeyEvent.KEYCODE_BACK)
//
//
//        return false;
//        // Disable back button..............
//    }
    private void onOrderProduct() {

    }
}
