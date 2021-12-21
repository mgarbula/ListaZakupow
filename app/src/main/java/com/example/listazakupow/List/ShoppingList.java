package com.example.listazakupow.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.listazakupow.Products.Product;

import java.util.ArrayList;

public class ShoppingList implements Parcelable {

    private String listName, shop;
    private ArrayList<Product> list;

    public ShoppingList(String listName, String shop, ArrayList<Product> list){
        this.listName = listName;
        this.shop = shop;
        this.list = list;
    }

    protected ShoppingList(Parcel in) {
        listName = in.readString();
        shop = in.readString();
        list = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public ArrayList<Product> getList() {
        return list;
    }

    public void setList(ArrayList<Product> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listName);
        dest.writeString(shop);
        dest.writeTypedList(list);
    }
}
