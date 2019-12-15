package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView item_cart_product_name, item_cart_price;

    public ImageView item_cart_product_image;

    public ElegantNumberButton item_cart_update_quantity;

    private ItemClickListener itemClickListener;

    public RelativeLayout cart_delete;
    public LinearLayout cart_foreground;

    public CartViewHolder(@NonNull View itemView, TextView item_cart_product_name) {
        super(itemView);
        this.item_cart_product_name = item_cart_product_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        item_cart_product_name = (TextView) itemView.findViewById(R.id.item_cart_product_name);
        item_cart_price = (TextView) itemView.findViewById(R.id.item_cart_price);

        item_cart_product_image = (ImageView) itemView.findViewById(R.id.item_cart_product_image);

        item_cart_update_quantity = (ElegantNumberButton) itemView.findViewById(R.id.item_cart_update_quantity);

        cart_delete = (RelativeLayout) itemView.findViewById(R.id.cart_delete);
        cart_foreground = (LinearLayout) itemView.findViewById(R.id.cart_foreground);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select action");
        menu.add(0, 0, getAdapterPosition(), Common.DELETE_CART);
    }
}