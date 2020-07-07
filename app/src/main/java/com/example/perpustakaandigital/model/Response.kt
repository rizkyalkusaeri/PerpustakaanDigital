package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Response(
    @SerializedName("status")
    @Expose
    val status : Boolean,

    @SerializedName("message")
    @Expose
    val message : String

)