package com.models;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.task_1.R;

public class ProductViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView img_product;
    public TextView txt_product_name, txt_time, txt_description;
    public CardView card_view;

    public ProductViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        img_product = (ImageView) itemView.findViewById(R.id.img_product);
        txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        card_view = (CardView) itemView.findViewById(R.id.card_view);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
