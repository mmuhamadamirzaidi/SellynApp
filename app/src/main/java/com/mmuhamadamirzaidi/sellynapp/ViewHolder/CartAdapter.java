package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.mmuhamadamirzaidi.sellynapp.Modules.Cart.CartActivity;
import com.mmuhamadamirzaidi.sellynapp.Modules.Checkout.CheckOutActivity;
import com.mmuhamadamirzaidi.sellynapp.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private CartActivity cart;
    private CheckOutActivity checkOutActivity;

    public CartAdapter(List<Order> listData, CartActivity cart) {
        this.listData = listData;
        this.cart = cart;
    }

    public CartAdapter(List<Order> listData, CheckOutActivity checkOutActivity) {
        this.checkOutActivity = checkOutActivity;

    }

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.item_cart, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int i) {


//        cartViewHolder.item_cart_count.setTypeface(ResourcesCompat.getFont(context.getApplicationContext(), R.font.mr));

//        cartViewHolder.item_cart_count.setNumber(listData.get(i).getQuantity());

        cartViewHolder.item_cart_update_quantity.setNumber(listData.get(i).getQuantity());

        cartViewHolder.item_cart_update_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(i);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //Update cart summary
                //Calculate grand total
                int sub_total_initial = 0, grand_total_initial = 0, delivery_charge = 0, others_charge = 0, discount = 0, total_charge;

                List<Order> orders = new Database(cart).getCart();

                for (Order item:orders) {
                    delivery_charge = (Integer.parseInt("6"));
                    others_charge = (Integer.parseInt("1"));
                    total_charge = delivery_charge + others_charge;

                    discount += (Integer.parseInt(item.getDiscount()));

                    sub_total_initial += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    grand_total_initial = sub_total_initial + total_charge - discount;
                }

                Locale locale = new Locale("en", "MY");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                cart.cart_sub_total.setText(fmt.format(sub_total_initial));
                cart.cart_delivery_charge.setText(fmt.format(delivery_charge));
                cart.cart_others_charge.setText(fmt.format(others_charge));
                cart.cart_discount.setText(fmt.format(discount));
                cart.cart_grand_total.setText(fmt.format(grand_total_initial));
            }
        });

        Locale locale = new Locale("en", "MY");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        int price = (Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));

        cartViewHolder.item_cart_product_name.setText(listData.get(i).getProductName());
        cartViewHolder.item_cart_price.setText(fmt.format(price));

        Picasso.with(cart.getApplicationContext())
                .load(listData.get(i).getProductImage())
                .into(cartViewHolder.item_cart_product_image);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position){
        listData.add(position, item);
        notifyItemInserted(position);
    }
}
