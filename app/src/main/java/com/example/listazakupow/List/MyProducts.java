package com.example.listazakupow.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.listazakupow.Products.Product;
import com.example.listazakupow.R;
import com.example.listazakupow.Recyclerviews.RecyclerViewAdapter;

import java.util.ArrayList;

public class MyProducts extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private ArrayList<Product> products;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        products = getIntent().getParcelableArrayListExtra("products");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, products, null, 1);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}