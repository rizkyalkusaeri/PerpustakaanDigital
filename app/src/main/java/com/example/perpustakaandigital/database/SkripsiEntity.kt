package com.example.perpustakaandigital.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tSkripsi")
data class SkripsiEntity (

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "skripsiId")
    var skripsiId: String? = null,

    @ColumnInfo(name = "nim_penulis")
    var nim_penulis : String? = null,

    @ColumnInfo(name = "penulis")
    var penulis : String? = null,

    @ColumnInfo(name = "judul_skripsi")
    var judul_skripsi : String? = null,

    @ColumnInfo(name = "kelompok_keilmuan")
    var kelompok_keilmuan : String? = null,

    @ColumnInfo(name = "file_pdf")
    var file_pdf : String? = null,

    @ColumnInfo(name = "pass_pdf")
    var pass_pdf : String? = null
) : Parcelable