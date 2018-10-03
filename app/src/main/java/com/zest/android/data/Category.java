package com.zest.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zest.android.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.parceler.Parcel;


@Parcel
public class Category {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("strCategory")
    @Expose
    private String title;

    @SerializedName("idCategory")
    @Expose
    private String categoryId;

    @SerializedName("strCategoryThumb")
    @Expose
    private String image;

    @SerializedName("strCategoryDescription")
    @Expose
    private String description;

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
