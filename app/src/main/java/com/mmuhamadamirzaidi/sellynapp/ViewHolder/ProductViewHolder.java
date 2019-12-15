package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView product_image, product_wishlist, product_share, product_quick_cart;
    public TextView product_name, product_notification;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_image = (ImageView) itemView.findViewById(R.id.product_image);

        product_quick_cart = (ImageView) itemView.findViewById(R.id.product_quick_cart);
        product_wishlist = (ImageView) itemView.findViewById(R.id.product_wishlist);
        product_share = (ImageView) itemView.findViewById(R.id.product_share);

        product_name = (TextView) itemView.findViewById(R.id.product_name);
        product_notification = (TextView) itemView.findViewById(R.id.product_notification);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
