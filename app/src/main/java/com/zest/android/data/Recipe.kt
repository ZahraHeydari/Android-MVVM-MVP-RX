package com.zest.android.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.greenrobot.greendao.annotation.Id
import org.greenrobot.greendao.annotation.Unique

@Entity
data class Recipe(@Id(autoincrement = true) @Unique var id: Long? = null,
                  @SerializedName("idMeal") @Expose @NonNull @PrimaryKey var recipeId: String,
                  @SerializedName("strMeal") @Expose var title: String? = null,
                  @SerializedName("strMealThumb") @Expose var image: String? = null,
                  @SerializedName("strInstructions") @Expose var instructions: String? = null,
                  @SerializedName("strTags") @Expose var tag: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(recipeId)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(instructions)
        parcel.writeString(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }

}