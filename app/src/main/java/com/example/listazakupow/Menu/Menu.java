package com.example.listazakupow.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.listazakupow.Dialogs.NewListDialog;
import com.example.listazakupow.List.MyLists;
import com.example.listazakupow.R;

public class Menu extends AppCompatActivity {

    private Button buttonNewList;
    private Button buttonLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        buttonNewList = findViewById(R.id.button_new_list);
        buttonNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openNewList();
                openNewListCreator();
            }
        });

        buttonLists = findViewById(R.id.button_lists);
        buttonLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLists();
            }
        });

    }

    //============================================
    //=================METODY=====================
    //============================================

    // metoda otwierająca okno początkowe nowej listy
    public void openNewListCreator(){
        NewListDialog nameOfNewList = new NewListDialog();
        nameOfNewList.show(getSupportFragmentManager(), "Nowa lista");
    }


    public void  openLists(){
        Intent intent = new Intent(this, MyLists.class);
        startActivity(intent);
    }

}