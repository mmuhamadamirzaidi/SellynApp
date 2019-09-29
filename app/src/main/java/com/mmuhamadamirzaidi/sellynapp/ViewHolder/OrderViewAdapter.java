package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.mmuhamadamirzaidi.sellynapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView item_order_detail_product_name, item_order_detail_product_price, item_order_detail_product_discount, item_order_detail_product_quantity;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        item_order_detail_product_name = (TextView) itemView.findViewById(R.id.item_order_detail_product_name);
        item_order_detail_product_price = (TextView) itemView.findViewById(R.id.item_order_detail_product_price);
        item_order_detail_product_discount = (TextView) itemView.findViewById(R.id.item_order_detail_product_discount);
        item_order_detail_product_quantity = (TextView) itemView.findViewById(R.id.item_order_detail_product_quantity);
    }
}

public class OrderViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

    //Check balik
    List<Order> orderList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_detail_product, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Order order = orderList.get(i);

        Locale locale = new Locale("en", "MY");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        myViewHolder.item_order_detail_product_name.setText(order.getProductName());
        myViewHolder.item_order_detail_product_price.setText(fmt.format(order.getPrice()));
        myViewHolder.item_order_detail_product_discount.setText(order.getDiscount());
        myViewHolder.item_order_detail_product_quantity.setText(order.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
