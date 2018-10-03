package com.zest.android.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.greenrobot.greendao.annotation.Id


data class Category(@Id(autoincrement = true) var id: Long? = null,
                    @SerializedName("strCategory") @Expose var title: String,
                    @SerializedName("idCategory") @Expose var categoryId: String? = null,
                    @SerializedName("strCategoryThumb") @Expose var image: String? = null,
                    @SerializedName("strCategoryDescription") @Expose var description: String? = null) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(categoryId)
        parcel.writeString(image)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}