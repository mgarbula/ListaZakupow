package com.example.listazakupow.Products;

import android.os.Parcel;
import android.os.Parcelable;
// implementuje Parcelable, bo przesyłam w NewList do Category
public class Product implements Parcelable{

    String name, unit;
    float quantity;

    public Product(String name, float quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected Product(Parcel in) {
        name = in.readString();
        unit = in.readString();
        quantity = in.readFloat();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeFloat(quantity);
    }
}
