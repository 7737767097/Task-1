package com.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AppDelegate;
import com.constants.Tags;
import com.interfaces.OnListItemClickListener;
import com.models.ProductModel;
import com.models.ProductViewHolders;
import com.task_1.R;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductViewHolders> {

    private ArrayList<ProductModel> productArray;
    private FragmentActivity mActivity;
    private OnListItemClickListener itemClickListener;

    public ProductRecyclerViewAdapter(FragmentActivity mActivity, ArrayList<ProductModel> productArray, OnListItemClickListener itemClickListener) {
        this.productArray = productArray;
        this.mActivity = mActivity;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ProductViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, null);
        ProductViewHolders rcv = new ProductViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolders holder, final int position) {
        try {
            holder.txt_time.setText(productArray.get(position).time);
            holder.txt_product_name.setText(productArray.get(position).title);
            holder.txt_description.setText(productArray.get(position).description);

            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // calling this below code, which is used to pass the item position to main class.
                    if (itemClickListener != null) {
                        itemClickListener.setOnListItemClickListener(Tags.NAME, position);
                    }
                }
            });
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    @Override
    public int getItemCount() {
        return this.productArray.size();
    }
}
