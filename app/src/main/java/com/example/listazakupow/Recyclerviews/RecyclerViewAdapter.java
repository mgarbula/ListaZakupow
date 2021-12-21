package com.example.listazakupow.Recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listazakupow.List.ShoppingList;
import com.example.listazakupow.Products.Product;
import com.example.listazakupow.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> mDataProduct;
    private ArrayList<ShoppingList> mDataShoppingList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int which;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, ArrayList<Product> dataProduct, ArrayList<ShoppingList> dataShoppingList, int which) {
        this.mInflater = LayoutInflater.from(context);
        this.which = which;
        if(which == 1) {
            this.mDataProduct = dataProduct;
            this.mDataShoppingList = new ArrayList<>();
        } else if(which == 2){
            this.mDataShoppingList = dataShoppingList;
            this.mDataProduct = new ArrayList<>();
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(which == 1) {
            Product product = mDataProduct.get(position);
            float quantity = product.getQuantity();
            String unit = product.getUnit();
            String productName = product.getName();
            if (quantity == 0.0)
                holder.myTextView.setText(productName);
            else if (quantity != 0)
                holder.myTextView.setText(productName + ", " + quantity + " " + unit);
        } else if(which == 2){
            ShoppingList shoppingList = mDataShoppingList.get(position);
            String listName = shoppingList.getListName();
            String shop = shoppingList.getShop();
            String text = listName + ", sklep: " + shop;
            holder.myTextView.setText(text);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        int size = 0;
        if(which == 1) {
            size =  mDataProduct.size();
        } else if(which == 2){
            size = mDataShoppingList.size();
        }
        return size;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}