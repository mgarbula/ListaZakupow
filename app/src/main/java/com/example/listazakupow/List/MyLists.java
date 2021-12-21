package com.example.listazakupow.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.listazakupow.Products.Product;
import com.example.listazakupow.R;
import com.example.listazakupow.Recyclerviews.RecyclerViewAdapter;
//import com.example.listazakupow.Recyclerviews.RecyclerViewAdapterForList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyLists extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener {

    private static String LIST_TO_SEND = "listToSend";
    private static String MY_LISTS = "myShoppingLists";
    private ArrayList<ShoppingList> myShoppingLists;
    private ShoppingList shoppingListToAdd;
    private RecyclerViewAdapter adapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        myShoppingLists = loadList();
        addList();
        showLists();
    }

    //==================================================

    public void addList(){
        SharedPreferences sharedPreferences = getSharedPreferences(LIST_TO_SEND, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyList", "");
        if(!json.equals("")) {
            shoppingListToAdd = gson.fromJson(json, ShoppingList.class);
            myShoppingLists.add(shoppingListToAdd);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            saveList();
        }
    }

    public void showLists(){
        myShoppingLists = loadList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, null, myShoppingLists, 2);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void saveList(){
        SharedPreferences sharedPreferences = getSharedPreferences(MY_LISTS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myShoppingLists);
        editor.putString("lists", json);
        editor.apply();
    }

    public ArrayList<ShoppingList> loadList(){
        SharedPreferences sharedPreferences = getSharedPreferences(MY_LISTS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lists", null);
        Type type = new TypeToken<ArrayList<ShoppingList>>() {}.getType();
        myShoppingLists = gson.fromJson(json, type);
        if(myShoppingLists == null){
            myShoppingLists = new ArrayList<>();
        }
        return myShoppingLists;
    }

    @Override
    public void onItemClick(View view, int position) {
        showPopupMenu(view, position);
    }

    public void setPosition(int pos){
        position = pos;
    }

    public int getPosition(){
        return position;
    }

    public void showPopupMenu(View view, int position){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        setPosition(position);
        popupMenu.inflate(R.menu.popup_menu_my_lists);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int pos = getPosition();
        switch (item.getItemId()){
            case R.id.open:
                Intent intent = new Intent(this, MyProducts.class);
                ArrayList<Product> products = myShoppingLists.get(position).getList();
                intent.putExtra("products", products);
                startActivity(intent);
                return true;
            case R.id.delete:
                myShoppingLists.remove(pos);
                saveList();
                showLists();
                return true;
            default:
                return false;
        }
    }
}