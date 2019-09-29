package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView item_order_id, item_order_date, item_order_price, item_order_status;
//    public ImageView item_order_edit, item_order_test_edit, item_order_edit_new;

//    public LinearLayout item_order_edit_layout;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        item_order_id = (TextView) itemView.findViewById(R.id.item_order_id);
//        item_order_date = (TextView) itemView.findViewById(R.id.item_order_date);
        item_order_price = (TextView) itemView.findViewById(R.id.item_order_price);
        item_order_status = (TextView) itemView.findViewById(R.id.item_order_status);

//        item_order_edit = (ImageView) itemView.findViewById(R.id.item_order_edit);
//        item_order_edit_new = (ImageView) itemView.findViewById(R.id.item_order_edit_new);
//        item_order_test_edit = (ImageView) itemView.findViewById(R.id.item_order_test_edit);
//        item_order_edit_layout = (LinearLayout) itemView.findViewById(R.id.item_order_edit_layout);

        itemView.setOnClickListener(this);
    }

    public OrderViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}