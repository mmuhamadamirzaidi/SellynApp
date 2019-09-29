package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.R;

public class BannedProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView banned_product_image;
    public TextView banned_product_name, banned_product_notification_no;

    private ItemClickListener itemClickListener;

    public BannedProductViewHolder(@NonNull View itemView) {
        super(itemView);

        banned_product_image = (ImageView) itemView.findViewById(R.id.banned_product_image);
        banned_product_name = (TextView) itemView.findViewById(R.id.banned_product_name);
        banned_product_notification_no = (TextView) itemView.findViewById(R.id.banned_product_notification_no);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
