package com.example.listazakupow.New_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.listazakupow.Categories.Category;
import com.example.listazakupow.Dialogs.AddProductDialog;
import com.example.listazakupow.List.MyLists;
import com.example.listazakupow.List.ShoppingList;
import com.example.listazakupow.Products.Product;
import com.example.listazakupow.R;
import com.example.listazakupow.Recyclerviews.RecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewList extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener, AddProductDialog.AddProductDialogListener{

    private static String SEND_PREFS = "sendPrefs";
    private static String LIST_TO_SEND = "listToSend";
    public static final String PREFS = "sPrefs";
    private static final String FIRST_START = "start";
    private static final String FRUITS_LIST = "fruitsList";
    private static final String VEGS_LIST = "vegsList";
    private static final String BREADS_LIST = "breadsList";
    private static final String NOODLES_LIST = "noodlesList";
    private static final String MEATS_LIST = "meatsList";
    private static final String DAIRY_LIST = "dairyList";
    private static final String SWEETS_LIST = "sweetsList";
    private static final String DRINKS_LIST = "drinksList";
    private static final String OTHERS_LIST = "othersList";
    private static final String NEW_LIST = "newList";
    private Button buttonOwoce;
    private Button buttonWarzywa;
    private Button buttonPieczywo;
    private Button buttonNabial;
    private Button buttonSypkie;
    private Button buttonMieso;
    private Button buttonSlodycze;
    private Button buttonNapoje;
    private Button buttonInne;
    private Button buttonSave;
    private RecyclerViewAdapter adapter;
    private ArrayList<Product> newList;
    private Product productToAdd;
    private ArrayList<Product> fruits;
    private ArrayList<Product> vegs;
    private ArrayList<Product> breads;
    private ArrayList<Product> dairy;
    private ArrayList<Product> noodles;
    private ArrayList<Product> meats;
    private ArrayList<Product> sweets;
    private ArrayList<Product> drinks;
    private ArrayList<Product> others;
    private ArrayList<Product> editedCategory;
    private int whichCategory, position;
    private String name, shop;
    private boolean onCreateBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_list);
        onCreateBoolean = true;

        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean(FIRST_START, true);
        //boolean firstStart = true;
        if(firstStart){
            primaryProducts();
        }

        if(newList == null){
            newList = new ArrayList<>();
        }

        showList();

        fruits = loadList(fruits, FRUITS_LIST);
        vegs = loadList(vegs, VEGS_LIST);
        breads = loadList(breads, BREADS_LIST);
        dairy = loadList(dairy, DAIRY_LIST);
        noodles = loadList(noodles, NOODLES_LIST);
        meats = loadList(meats, MEATS_LIST);
        sweets = loadList(sweets, SWEETS_LIST);
        drinks = loadList(drinks, DRINKS_LIST);
        others = loadList(others, OTHERS_LIST);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            name = extras.getString("name");
            shop = extras.getString("shop");
        }

        buttonOwoce = findViewById(R.id.button_owoce);
        buttonOwoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(fruits, FRUITS_LIST);
                whichCategory = 1;
            }
        });

        buttonWarzywa = findViewById(R.id.button_warzywa);
        buttonWarzywa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(vegs, VEGS_LIST);
                whichCategory = 2;
            }
        });

        buttonPieczywo = findViewById(R.id.button_pieczywo);
        buttonPieczywo.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(breads, BREADS_LIST);
                whichCategory = 3;
            }
        }));

        buttonNabial = findViewById(R.id.button_nabial);
        buttonNabial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(dairy, DAIRY_LIST);
                whichCategory = 4;
            }
        });

        buttonSypkie = findViewById(R.id.button_sypkie);
        buttonSypkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(noodles, NOODLES_LIST);
                whichCategory = 5;
            }
        });

        buttonMieso = findViewById(R.id.button_mieso);
        buttonMieso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(meats, MEATS_LIST);
                whichCategory = 6;
            }
        });

        buttonSlodycze = findViewById(R.id.button_slodycze);
        buttonSlodycze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(sweets, SWEETS_LIST);
                whichCategory = 7;
            }
        });

        buttonNapoje = findViewById(R.id.button_napoje);
        buttonNapoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(drinks, DRINKS_LIST);
                whichCategory = 8;
            }
        });

        buttonInne = findViewById(R.id.button_inne);
        buttonInne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory(others, OTHERS_LIST);
                whichCategory = 9;
            }
        });

        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                for(int i = newList.size() - 1; i >= 0; i--) {
                    newList.remove(i);
                    saveList(newList, NEW_LIST);
                }
                finish();
            }
        });
    }

    // tak muszę zrobić, żeby nie było problemu z powrotem z Category, żeby się nie nakładało i żeby się dobrze zapisywało
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SEND_PREFS, MODE_PRIVATE);
        boolean productSent = sharedPreferences.getBoolean("productSent", false);
        if(!onCreateBoolean && productSent) {
            String productName = sharedPreferences.getString("productName", "");
            float quantity = sharedPreferences.getFloat("productQuantity", 0);
            String unit = sharedPreferences.getString("productUnit", "");
            productToAdd = new Product(productName, quantity, unit);
            newList.add(productToAdd);
            saveList(newList, NEW_LIST);
            showList();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        onCreateBoolean = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                editedCategory = new ArrayList<>();
                editedCategory = data.getParcelableArrayListExtra("editedCategory");
                if(editedCategory != null) {
                    switch (whichCategory) {
                        case 1:
                            fruits = editedCategory;
                            saveList(fruits, FRUITS_LIST);
                            break;
                        case 2:
                            vegs = editedCategory;
                            saveList(vegs, VEGS_LIST);
                            break;
                        case 3:
                            breads = editedCategory;
                            saveList(breads, BREADS_LIST);
                            break;
                        case 4:
                            dairy = editedCategory;
                            saveList(dairy, DAIRY_LIST);
                            break;
                        case 5:
                            noodles = editedCategory;
                            saveList(noodles, NOODLES_LIST);
                            break;
                        case 6:
                            meats = editedCategory;
                            saveList(meats, MEATS_LIST);
                            break;
                        case 7:
                            sweets = editedCategory;
                            saveList(sweets, SWEETS_LIST);
                            break;
                        case 8:
                            drinks = editedCategory;
                            saveList(drinks, DRINKS_LIST);
                            break;
                        case 9:
                            others = editedCategory;
                            saveList(others, OTHERS_LIST);
                            break;
                    }
                }
            }
        }
    }

    //===============================================

    public void showList(){
        newList = loadList(newList, NEW_LIST);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, newList, null, 1);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Product> loadList(ArrayList list, String name){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(name, null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        list = gson.fromJson(json, type);
        //Toast.makeText(this, ""+list.size(), Toast.LENGTH_SHORT).show();
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    // funkcja do testów służąca do usuwania całej nowej listy
    public void delete(){
        loadList(newList, NEW_LIST);
        for(int i = newList.size() - 1; i >= 0; i--){
            newList.remove(i);
        }
        saveList(newList, NEW_LIST);
    }

    public void saveList(ArrayList list, String name){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(name, json);
        editor.apply();
    }

    public void primaryProducts(){
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FIRST_START, false);
        editor.apply();

        fruits = new ArrayList<>();
        fruits.add(new Product("Jabłko", 0, "kg"));
        fruits.add(new Product("Gruszka", 0, "kg"));
        fruits.add(new Product("Banan", 0, "kg"));
        fruits.add(new Product("Brzoskwinia", 0, "kg"));
        fruits.add(new Product("Sliwka", 0, "kg"));
        fruits.add(new Product("Winogrona", 0, "kg"));
        fruits.add(new Product("Jagody", 0, "kg"));
        fruits.add(new Product("Maliny", 0, "kg"));
        fruits.add(new Product("Truskawki", 0, "kg"));
        saveList(fruits, FRUITS_LIST);

        vegs = new ArrayList<>();
        vegs.add(new Product("Marchew", 0, "kg"));
        vegs.add(new Product("Pomidor", 0, "kg"));
        vegs.add(new Product("Ogórek zielony", 0, "kg"));
        vegs.add(new Product("Sałata", 0, "kg"));
        vegs.add(new Product("Cebula", 0, "kg"));
        vegs.add(new Product("Czosnek", 0, "kg"));
        vegs.add(new Product("Ziemniaki", 0, "kg"));
        vegs.add(new Product("Papryka", 0, "kg"));
        saveList(vegs, VEGS_LIST);

        breads = new ArrayList<>();
        breads.add(new Product("Bułka przenna", 0, ""));
        breads.add(new Product("Bułka pełnoziarnista", 0, ""));
        breads.add(new Product("Grahamka", 0, ""));
        breads.add(new Product("Chleb pełnoziarnisty", 0, ""));
        breads.add(new Product("Chleb z żurawiną", 0, ""));
        breads.add(new Product("Rogal", 0, ""));
        breads.add(new Product("Bułka maślana", 0, ""));
        breads.add(new Product("Drożdżówka z posypką", 0, ""));
        saveList(breads, BREADS_LIST);

        dairy = new ArrayList<>();
        dairy.add(new Product("Mleko", 0, ""));
        dairy.add(new Product("Jogurt owocowy", 0, ""));
        dairy.add(new Product("Serek wiejski", 0, ""));
        dairy.add(new Product("Jaja", 0, ""));
        dairy.add(new Product("Smietana 30%", 0, ""));
        dairy.add(new Product("Smietana 18%", 0, ""));
        dairy.add(new Product("Smietana 12%", 0, ""));
        saveList(dairy, DAIRY_LIST);

        noodles = new ArrayList<>();
        noodles.add(new Product("Makaron", 0, ""));
        noodles.add(new Product("Ryż długoziarnisty", 0, ""));
        noodles.add(new Product("Mąka pszenna", 0, ""));
        noodles.add(new Product("Kasza jaglana", 0, ""));
        noodles.add(new Product("Kasza gryczana", 0, ""));
        noodles.add(new Product("Kasza pęczak", 0, ""));
        noodles.add(new Product("Cukier", 0, ""));
        saveList(noodles, NOODLES_LIST);

        meats = new ArrayList<>();
        meats.add(new Product("Pierś z kurzczaka", 0, "kg"));
        meats.add(new Product("Wołowe mięso mielone", 0, "kg"));
        meats.add(new Product("Salami", 0, "kg"));
        meats.add(new Product("Stek", 0, "kg"));
        meats.add(new Product("Szynka drobiowa", 0, "kg"));
        meats.add(new Product("Biała parzona", 0, "kg"));
        meats.add(new Product("Frankfuterki", 0, "kg"));
        meats.add(new Product("Krupniok", 0, "kg"));
        saveList(meats, MEATS_LIST);

        sweets = new ArrayList<>();
        sweets.add(new Product("Mleczna czekolada", 0, ""));
        sweets.add(new Product("Gorzka czekolada", 0, ""));
        sweets.add(new Product("Biała czekolada", 0, ""));
        sweets.add(new Product("Rurki z kremem", 0, ""));
        sweets.add(new Product("Snickers", 0, ""));
        sweets.add(new Product("Mars", 0, ""));
        sweets.add(new Product("Prince polo", 0, ""));
        saveList(sweets, SWEETS_LIST);

        drinks = new ArrayList<>();
        drinks.add(new Product("Woda niegazowana", 0, "l"));
        drinks.add(new Product("Woda gazowana", 0, "l"));
        drinks.add(new Product("Sok jabłkowy", 0, "l"));
        drinks.add(new Product("Sok pomarańczowy", 0, "l"));
        drinks.add(new Product("Coca cola", 0, "l"));
        drinks.add(new Product("Sprite", 0, "l"));
        drinks.add(new Product("Fanta", 0, "l"));
        drinks.add(new Product("Tonic", 0, "l"));
        saveList(drinks, DRINKS_LIST);

        others = new ArrayList<>();
        others.add(new Product("Papier toaletpwy", 0, ""));
        others.add(new Product("Pasta do zębów", 0, ""));
        others.add(new Product("Ręczniki papierowe", 0, ""));
        others.add(new Product("Płyn do mycia naczyń", 0, ""));
        saveList(others, OTHERS_LIST);
    }


    // metoda zwracająca Intent --> przesyłanie do Category
    public void openCategory(ArrayList category, String name){
        category = loadList(category, name);
        Intent intent = new Intent(NewList.this, Category.class);
        // przesyłanie ArrayList do innego Activity (Category)
        // klasa, któej obiekty są w tabilcy musi implementować Parcelable
        intent.putParcelableArrayListExtra("key", category);
        startActivityForResult(intent, 1);
        //finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        showMenu(view, position);
    }

    public void setPosition(int position) { this.position = position; }

    public int getPosition() { return position; }

    // musi być interface
    public void showMenu(View view, int position){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        setPosition(position);
        popupMenu.inflate(R.menu.new_list_popup_menu);
        popupMenu.show();
    }

    public void editProduct(String name, String unit){
        // potrzebny interface z tym Dialogiem
        AddProductDialog addProductDialog = new AddProductDialog(name, unit, "NewList");
        addProductDialog.show(getSupportFragmentManager(), "Edytuj produkt");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int pos = getPosition();
        String name = newList.get(pos).getName();
        String unit = newList.get(pos).getUnit();
        switch (item.getItemId()){
            case R.id.edit:
                editProduct(name, unit);
                return true;
            case R.id.delete:
                newList.remove(pos);
                saveList(newList, NEW_LIST);
                showList();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void addProduct(String product, float mass, String unit) {}

    @Override
    public void editProductFromList(String product, float quantity, String unit) {
        newList.get(getPosition()).setQuantity(quantity);
        saveList(newList, NEW_LIST);
        showList();
    }

    public void save(){
        ShoppingList shoppingList = new ShoppingList(name, shop, newList);
        SharedPreferences sharedPreferences = getSharedPreferences(LIST_TO_SEND, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // zapisywanie obiektów własnych klas
        Gson gson = new Gson();
        String json = gson.toJson(shoppingList);
        editor.putString("MyList", json);
        //editor.putBoolean("wasSaved", true);
        editor.commit();
        Toast.makeText(this, "Zapisano listę. Przejdź do Moje Listy.", Toast.LENGTH_SHORT).show();
    }

}