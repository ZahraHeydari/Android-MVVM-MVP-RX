package com.zest.android.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class CategoryResponse(@SerializedName("categories") @Expose var categories: List<Category> = ArrayList()) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Category))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryResponse> {
        override fun createFromParcel(parcel: Parcel): CategoryResponse {
            return CategoryResponse(parcel)
        }

        override fun newArray(size: Int): Array<CategoryResponse?> {
            return arrayOfNulls(size)
        }
    }

}