package com.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bharat on 06/27/2016.
 */
public class ProductModel implements Parcelable {

    public String id, time, title, description;

    protected ProductModel(Parcel in) {
        id = in.readString();
        time = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public ProductModel(String id, String title, String time, String description) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.description = description;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(description);
    }
}
