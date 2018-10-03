package com.zest.android.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class RecipeResponse(@SerializedName("meals") @Expose var recipes: List<Recipe> = ArrayList()) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Recipe))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(recipes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeResponse> {
        override fun createFromParcel(parcel: Parcel): RecipeResponse {
            return RecipeResponse(parcel)
        }

        override fun newArray(size: Int): Array<RecipeResponse?> {
            return arrayOfNulls(size)
        }
    }

}