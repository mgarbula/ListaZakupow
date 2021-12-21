package com.example.listazakupow.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.listazakupow.R;

public class EditProductDialog extends AppCompatDialogFragment {

    private EditText editTextProduct;
    private Context context;
    private EditProductDialogListener listener;
    private String productName;

    public EditProductDialog(String productName){
        this.productName = productName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // do całego okna
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // do mojego layoutu
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        context = getContext();

        builder.setTitle("Edytuj nazwę produktu")
                .setView(layoutInflater.inflate(R.layout.activity_edit_product_dialog, null))
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Edytuj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(layoutInflater.inflate(R.layout.activity_edit_product_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        // to musi być po utworzeniu obiektu AlertDialog i dialog.findViewById...
        editTextProduct = dialog.findViewById(R.id.name);
        editTextProduct.setText(productName);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = editTextProduct.getText().toString();
                if(productName.isEmpty()){
                    Toast.makeText(context, "Edytuj nazwę produktu", Toast.LENGTH_SHORT).show();
                } else {
                    listener.applyEditProductName(productName); //używam mojego interface
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }

    //==============================================
    //==========DO PRZESŁANIA DO FRUITS=============
    //==============================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditProductDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement EditProductDialogListener");
        }
    }

    // interface
    public interface EditProductDialogListener {
        void applyEditProductName(String newProduct);
    }
}
