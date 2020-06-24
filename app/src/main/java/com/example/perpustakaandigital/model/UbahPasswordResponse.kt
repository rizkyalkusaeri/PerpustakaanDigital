package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UbahPasswordResponse(
    @SerializedName("status")
    @Expose
    val status : Boolean,

    @SerializedName("message")
    @Expose
    val message : String,

    @SerializedName("data")
    @Expose
    val ubah : UbahPassword
)

@Parcelize
data class UbahPassword(
    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null
) : Parcelable