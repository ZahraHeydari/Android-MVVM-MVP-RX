package com.zest.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.parceler.Parcel;


@Parcel
@Entity
public class Recipe {

    @Id(autoincrement = true)
    @Unique
    private Long id;
    @SerializedName("idMeal")
    @Expose
    private String recipeId;
    @SerializedName("strMeal")
    @Expose
    private String title;
    @SerializedName("strMealThumb")
    @Expose
    private String image;
    @SerializedName("strInstructions")
    @Expose
    private String instructions;
    @SerializedName("strTags")
    @Expose
    private String tag;

    @Generated(hash = 540407175)
    public Recipe(Long id, String recipeId, String title, String image,
                  String instructions, String tag) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.image = image;
        this.instructions = instructions;
        this.tag = tag;
    }

    @Generated(hash = 829032493)
    public Recipe() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRecipeId() {
        return this.recipeId;
    }
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getInstructions() {
        return this.instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipeId='" + recipeId + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", instructions='" + instructions + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
