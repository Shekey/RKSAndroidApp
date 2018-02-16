package com.example.ajdin.probafragmenti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.ajdin.probafragmenti.R;
import com.example.ajdin.probafragmenti.constant.Constant;
import com.example.ajdin.probafragmenti.model.CartItem;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class CartItemAdapter extends BaseAdapter {
    private static final String TAG = "CartItemAdapter";

    public List<CartItem> cartItems = Collections.emptyList();

    private final Context context;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public CartItemAdapter(Context context) {
        this.context = context;
    }

    public void updateCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public CartItem getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tvName;

        TextView tvPrice;

//adapter_cart_item layout
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_cart_item, parent, false);
            tvName = (TextView) convertView.findViewById(R.id.tvCartItemName);
//            tvUnitPrice = (TextView) convertView.findViewById(R.id.tvCartItemUnitPrice);
//            tvQuantity = (TextView) convertView.findViewById(R.id.tvCartItemQuantity);
            tvPrice = (TextView) convertView.findViewById(R.id.tvCartItemPrice);
            convertView.setTag(new ViewHolder(tvName, tvPrice));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            tvName = viewHolder.tvCartItemName;

            tvPrice = viewHolder.tvCartItemPrice;
        }

        final Cart cart = CartHelper.getCart();
        final CartItem cartItem = getItem(position);
        tvName.setText(cartItem.getProduct().getName());
        tvPrice.setText(String.valueOf(cart.getCost(cartItem.getProduct()).setScale(2, BigDecimal.ROUND_HALF_UP)+" "+Constant.CURRENCY));
        return convertView;
    }

    private static class ViewHolder {
        public final TextView tvCartItemName;

        public final TextView tvCartItemPrice;

        public ViewHolder(TextView tvCartItemName,  TextView tvCartItemPrice) {
            this.tvCartItemName = tvCartItemName;

            this.tvCartItemPrice = tvCartItemPrice;
        }
    }
}
