package com.example.listazakupow.Dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listazakupow.R;

public class AddProductDialog extends AppCompatDialogFragment {

    private Context context;
    private NumberPicker numberPicker;
    private TextView productNameTextView, productUnitTextView;
    private String productName, unit, fromWhichClass;
    private AddProductDialogListener listener;

    public AddProductDialog(String productName, String unit, String fromWhichClass){
        this.productName = productName;
        this.unit = unit;
        this.fromWhichClass = fromWhichClass;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // do całego okna
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // do mojego layoutu
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        context = getContext();

        if(fromWhichClass.equals("Category")) {
            builder.setTitle("Dodaj produkt do listy zakupów");
        } else if(fromWhichClass.equals("NewList")){
            builder.setTitle("Edytuj produkt");
        }
        builder.setView(layoutInflater.inflate(R.layout.activity_add_product_dialog, null))
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(layoutInflater.inflate(R.layout.activity_add_product_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        productNameTextView = dialog.findViewById(R.id.name);
        productNameTextView.setText(productName);

        productUnitTextView = dialog.findViewById(R.id.product_unit);
        productUnitTextView.setText(unit);
        numberPicker = dialog.findViewById(R.id.number_picker);
        if(unit.equals("")) {
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(15);
        } else if(unit.equals("l")){
            NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    float difference = (float) (value * 0.5);
                    return "" + difference;
                }
            };
            numberPicker.setFormatter(formatter);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(10);
        } else if(unit.equals("kg")) {
            NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    float difference = (float) (value * 0.1);
                    return "" + difference;
                }
            };
            numberPicker.setFormatter(formatter);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(50);
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float productQuantity = numberPicker.getValue();
                if(unit.equals("kg")) {
                    productQuantity *= .1;
                } else if(unit.equals("l")) {
                    productQuantity *= .5;
                }
                if(fromWhichClass.equals("Category")) {
                    listener.addProduct(productName, productQuantity, unit); //używam mojego interface
                    Toast.makeText(context, "Dodano do listy", Toast.LENGTH_SHORT).show();
                } else if(fromWhichClass.equals("NewList")){
                    listener.editProductFromList(productName, productQuantity, unit); //używam mojego interface
                    Toast.makeText(context, "Zapisano", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddProductDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement EditProductDialogListener");
        }
    }

    public interface AddProductDialogListener {
        void addProduct(String product, float mass, String unit);
        void editProductFromList(String product, float mass, String unit);
    }
}