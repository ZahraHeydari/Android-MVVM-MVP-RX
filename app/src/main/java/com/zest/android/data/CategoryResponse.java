package com.zest.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class CategoryResponse {


    @SerializedName("categories")
    @Expose
    List<Category> categories = new ArrayList<>();


    public CategoryResponse() {
    }

    public CategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "categories=" + categories +
                '}';
    }
}
