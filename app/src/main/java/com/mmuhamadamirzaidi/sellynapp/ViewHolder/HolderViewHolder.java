package com.mmuhamadamirzaidi.sellynapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.R;

public class HolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView holder_image;
    public TextView holder_name, holder_phone;

    private ItemClickListener itemClickListener;

    public HolderViewHolder(@NonNull View itemView) {
        super(itemView);

        holder_image = (ImageView) itemView.findViewById(R.id.holder_image);
        holder_name = (TextView) itemView.findViewById(R.id.holder_name);
        holder_phone = (TextView) itemView.findViewById(R.id.holder_phone);

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
