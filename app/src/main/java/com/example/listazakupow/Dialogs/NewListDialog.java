package com.example.listazakupow.Dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.listazakupow.New_list.NewList;
import com.example.listazakupow.R;


public class NewListDialog extends AppCompatDialogFragment {

    private Context context;
    private EditText editTextName, editTextShop;
    private String name, shop;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // do całego okna
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // do mojego layoutu
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        context = getContext();

        builder.setTitle("Nowa lista")
                .setView(layoutInflater.inflate(R.layout.activity_new_list_dialog, null))
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Dalej", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setView(layoutInflater.inflate(R.layout.activity_new_list_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();
        // to musi być po utworzeniu obiektu AlertDialog i dialog.findViewById...
        editTextName = dialog.findViewById(R.id.new_list_name);
        editTextShop = dialog.findViewById(R.id.shop_name);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString();
                shop = editTextShop.getText().toString();

                if(name.isEmpty() && shop.isEmpty()){
                    Toast.makeText(context, "Podaj nazwę listy i sklepu", Toast.LENGTH_SHORT).show();
                } else if(shop.isEmpty()){
                    Toast.makeText(context, "Podaj nazwę sklepu", Toast.LENGTH_SHORT).show();
                } else if(name.isEmpty()){
                    Toast.makeText(context, "Podaj nazwę listy", Toast.LENGTH_SHORT).show();
                } else if(!name.isEmpty() && !shop.isEmpty()){
                    startNewList();
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }

    //============================================
    //=================METODY=====================
    //============================================

    public void startNewList() {
        Intent intent = new Intent(context, NewList.class);
        intent.putExtra("name", name);
        intent.putExtra("shop", shop);
        context.startActivity(intent);
    }

}
