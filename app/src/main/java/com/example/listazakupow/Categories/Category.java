package com.example.listazakupow.Categories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listazakupow.Dialogs.AddProductDialog;
import com.example.listazakupow.Dialogs.EditProductDialog;
import com.example.listazakupow.Dialogs.NewProductDialog;
import com.example.listazakupow.New_list.NewList;
import com.example.listazakupow.Products.Product;
import com.example.listazakupow.R;
import com.example.listazakupow.Recyclerviews.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Category extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener, NewProductDialog.NewProductDialogListener, PopupMenu.OnMenuItemClickListener, EditProductDialog.EditProductDialogListener, AddProductDialog.AddProductDialogListener {

    private static String SEND_PREFS = "sendPrefs";
    private RecyclerViewAdapter adapter;
    private ArrayList<Product> products;
    private FloatingActionButton fab;
    private Button saveButton;
    private Context context;
    private int position;
    private int startArraySize;
    private ArrayList<Product> newProductsInCategory; // do przesłania do NewList jak doda się do kategorii

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_category);
        context = this;

        newProductsInCategory = new ArrayList<>();
        products = new ArrayList<>();
        // odbieranie ArrayList z NewList
        products = getIntent().getParcelableArrayListExtra("key");
        showData();
        startArraySize = products.size();

        fab = findViewById(R.id.button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Dodaj produkt do kategorii", Toast.LENGTH_SHORT).show();
                openNewProductCreator();
            }
        });

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category.this, NewList.class);
                intent.putParcelableArrayListExtra("editedCategory", products);
                setResult(RESULT_OK, intent);
                Toast.makeText(Category.this, "Zapisano kategorię", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //================================================

    public void showData(){
        //loadData();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, products, null, 1);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // otwarcie Dialog
    public void openNewProductCreator(){
        NewProductDialog newProductDialog = new NewProductDialog();
        newProductDialog.show(getSupportFragmentManager(), "Dodaj nowy produkt");
    }

    @Override
    public void applyNewProductName(String newProduct) {
        String unit = products.get(products.size() - 2).getUnit();
        products.add(new Product(newProduct, 0, unit));
        showData();
    }

    // z interface w RecyclerViewAdapter
    @Override
    public void onItemClick(View view, int position) {
        showPopupMenu(view, position);
    }

    // pokazuje popupMenu, musi być interface
    public void showPopupMenu(View view, int position){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        setPosition(position);
        popupMenu.inflate(R.menu.category_popup_menu);
        popupMenu.show();
    }

    public void setPosition(int pos){
        position = pos;
    }

    public int getPosition(){
        return position;
    }

    public void editData(String name){
        EditProductDialog editProductDialog = new EditProductDialog(name);
        editProductDialog.show(getSupportFragmentManager(), "Dodaj nowy produkt");
    }

    @Override
    public void applyEditProductName(String newProduct) {
        products.get(getPosition()).setName(newProduct);
        showData();
    }

    public void addProductToListDialog(String name, String unit){
        AddProductDialog addProductDialog = new AddProductDialog(name, unit, "Category");
        addProductDialog.show(getSupportFragmentManager(), "Dodaj produkt do listy zakupów");
    }

    @Override
    public void addProduct(String product, float quantity, String unit) {
        // tak muszę zrobić, żeby nie było problemu z powrotem do NewList, żeby się nie nakładało i żeby się dobrze zapisywało
        SharedPreferences sharedPreferences = getSharedPreferences(SEND_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("productName", product);
        editor.putFloat("productQuantity", quantity);
        editor.putString("productUnit", unit);
        editor.putBoolean("productSent", true);
        editor.apply();
        finish();
    }

    @Override
    public void editProductFromList(String product, float mass, String unit) {}

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int pos = getPosition();
        String name = products.get(pos).getName();
        String unit = products.get(pos).getUnit();
        switch (item.getItemId()){
            case R.id.add:
                addProductToListDialog(name, unit);
                return true;
            case R.id.edit:
                editData(name);
                return true;
            case R.id.delete:
                products.remove(position);
                showData();
                Toast.makeText(this, "Usunięto produkt: " + name, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

}
